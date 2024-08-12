
package org.traccar.session.cache;

import org.traccar.model.BaseModel;

record CacheKey(Class<? extends BaseModel> clazz, long id) {
    CacheKey(BaseModel object) {
        this(object.getClass(), object.getId());
    }
}
