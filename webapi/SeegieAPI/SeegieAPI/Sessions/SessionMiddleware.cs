﻿///
///   Copyright 2017 Mikhail Vasilyev
///
///   Licensed under the Apache License, Version 2.0 (the "License");
///   you may not use this file except in compliance with the License.
///   You may obtain a copy of the License at
///
///       http://www.apache.org/licenses/LICENSE-2.0
///
///   Unless required by applicable law or agreed to in writing, software
///   distributed under the License is distributed on an "AS IS" BASIS,
///   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
///   See the License for the specific language governing permissions and
///   limitations under the License.
///
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Http;
using System.Net.WebSockets;
using SeegieAPI.Registration;

namespace SeegieAPI.Sessions
{
    public class SessionMiddleware
    {
        private readonly RequestDelegate _next;
        private SessionManager _manager;
        private IGuidService _guidFactory;

        public SessionMiddleware(RequestDelegate next, IGuidService guidFactory)
        {
            _next = next;
            _manager = new SessionManager((id) => guidFactory.FreeId(id));
            _guidFactory = guidFactory;
        }
        public async Task Invoke(HttpContext ctx)
        {
            if (ctx.Request.Query.Count == 2) {
                string sessionId = ctx.Request.Query["sessionid"];
                Guid id = Guid.Parse(sessionId);
                string role = ctx.Request.Query["role"];

                if (_guidFactory.IsIdTaken(id)) {
                    if (role == "leech") {
                        WebSocket ws = await ctx.WebSockets.AcceptWebSocketAsync();
                        var handler = _manager.AddLeech(id, ws);
                        await handler.ListenAsync();
                    }
                    else if (role == "seed") {
                        WebSocket ws = await ctx.WebSockets.AcceptWebSocketAsync();
                        var handler = _manager.AddSeed(id, ws);
                        await handler.ListenAsync();
                    }
                    else {
                        ctx.Response.StatusCode = 400;
                    }
                    return;
                }
            }
            ctx.Response.StatusCode = 404;
        }
    }
    public static class SessionMiddlewareExtensions
    {
        public static IApplicationBuilder MapSessionMiddleware(this IApplicationBuilder builder)
        {
            return builder.MapWhen(
                predicate: httpCtx => httpCtx.WebSockets.IsWebSocketRequest,
                configuration: app => app.UseMiddleware<SessionMiddleware>()
            );
        }
    }
}