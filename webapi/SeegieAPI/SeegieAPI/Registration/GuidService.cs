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
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Diagnostics;

namespace SeegieAPI.Registration
{
    public interface IGuidService
    {
        Guid ReserveNextId();
        bool TryUseId(Guid id);
        bool IsIdUsed(Guid id);
        void FreeId(Guid id);
    }

    public class GuidService : IGuidService
    {
        private class LifetimeCounter
        {
            public int Counter { get; set; }
            public static implicit operator LifetimeCounter(int count)
            {
                return new LifetimeCounter { Counter = count };
            }
        }
        private readonly IDictionary<Guid, LifetimeCounter> _reservedIds;
        private readonly ISet<Guid> _inUseIds;
        private const int INITIAL_LIFETIME = 20; // max number of reserved ids at any time

        public GuidService()
        {
            _reservedIds = new ConcurrentDictionary<Guid, LifetimeCounter>();
            _inUseIds = new SortedSet<Guid>();
        }
        public Guid ReserveNextId()
        {
            // generate new id and add to reserved
            Guid id;
            do {
                id = Guid.NewGuid();
            } while (_reservedIds.ContainsKey(id) || _inUseIds.Contains(id));
            _reservedIds.Add(id, INITIAL_LIFETIME);

            // decrement all counters and remove those getting below 0
            foreach (var tuple in _reservedIds) {
                if (--tuple.Value.Counter < 0)
                    _reservedIds.Remove(tuple.Key);
            }
            //Debug.WriteLine($"Reserved ids count = {_reservedIds.Count}");
            //Debug.WriteLine($"In-use ids count = {_inUseIds.Count}");
            return id;
        }
        public bool TryUseId(Guid id)
        {
            // move id from reserved to in-use
            bool reserved = _reservedIds.ContainsKey(id);
            if (reserved) {
                _reservedIds.Remove(id);
                _inUseIds.Add(id);
            }
            return reserved;
        }
        public bool IsIdUsed(Guid id)
        {
            return _inUseIds.Contains(id);
        }
        public void FreeId(Guid id)
        {
            _inUseIds.Remove(id);
        }

    }
}
