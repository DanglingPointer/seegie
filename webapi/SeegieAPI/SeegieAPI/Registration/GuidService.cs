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

namespace SeegieAPI.Registration
{
    public interface IGuidService
    {
        Guid GetNextId();
        bool IsIdTaken(Guid id);
        void FreeId(Guid id);
    }
    public class GuidService : IGuidService
    {
        private readonly SortedSet<Guid> _takenIds = new SortedSet<Guid>();
        
        public Guid GetNextId()
        {
            Guid id;
            do {
                id = Guid.NewGuid();
            } while (_takenIds.Contains(id));
            _takenIds.Add(id);
            return id;
        }
        public bool IsIdTaken(Guid id)
        {
            return _takenIds.Contains(id);
        }
        public void FreeId(Guid id)
        {
            _takenIds.Remove(id);        
        }
    }
}
