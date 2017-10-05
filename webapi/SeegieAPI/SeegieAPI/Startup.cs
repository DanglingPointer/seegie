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
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using SeegieAPI.Sessions;
using SeegieAPI.Registration;

namespace SeegieAPI
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }
        
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddSingleton<IGuidService, GuidService>();
        }
        public void Configure(IApplicationBuilder app, IHostingEnvironment env, IServiceProvider services)
        {
            if (env.IsDevelopment()) {
                app.UseDeveloperExceptionPage();
            }
            app.UseWebSockets();
            app.MapSessionMiddleware();
            app.UseRegistrationMiddleware();
        }
    }
}
