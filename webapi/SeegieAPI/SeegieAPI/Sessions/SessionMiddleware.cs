///
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
using System.Diagnostics;

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
            try {
                if (ctx.Request.Query.Count != 2)
                    throw new Exception("Illegal number of request arguments");

                string sessionId = ctx.Request.Query["sessionid"];
                Guid id = Guid.Parse(sessionId);
                string role = ctx.Request.Query["role"];

                if (_guidFactory.TakeInUseId(id)) {
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
                        throw new Exception("Illegal request arguments");
                    }
                }
            }
            catch (Exception ex) {
                if (!ctx.Response.HasStarted)
                    ctx.Response.StatusCode = 400;
                Debug.WriteLine(ex.ToString());
            }
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
