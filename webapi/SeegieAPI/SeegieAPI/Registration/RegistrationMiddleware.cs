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

namespace SeegieAPI.Registration
{
    public class RegistrationMiddleware
    {
        private readonly RequestDelegate _next;
        private IGuidService _guidFactory;

        public RegistrationMiddleware(RequestDelegate next, IGuidService guidFactory)
        {
            _next = next;
            _guidFactory = guidFactory;
        }

        public async Task Invoke(HttpContext ctx)
        {
            await ctx.Response.WriteAsync(_guidFactory.ReserveNextId().ToString());
        }
    }
    public static class RegistrationMiddlewareExtensions
    {
        public static IApplicationBuilder UseRegistrationMiddleware(this IApplicationBuilder builder)
        {
            return builder.UseMiddleware<RegistrationMiddleware>();
        }
    }
}
