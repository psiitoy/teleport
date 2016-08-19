package org.sprintdragon.teleport.core.migrate;

import org.sprintdragon.teleport.core.constants.TeleportConstants;
import org.sprintdragon.teleport.persistent.dao.EntityDao;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by wangdi on 16-8-15.
 */
public class PersistentManager implements IPersistentManager {

    @Resource
    Map<String, EntityDao> entityDaoMap;

    @Resource
    Map<String, ICheckMigrate> checkMigrateMap;

    @Override
    public EntityDao findEntityDaoByKey(String key) {
        return entityDaoMap.get(key);
    }

    @Override
    public ICheckMigrate findCheckMigrateByKey(String fromDbKey, String toDbKey) {
        return checkMigrateMap.get(fromDbKey.concat(TeleportConstants.SYMBOL_FROM_TO).concat(toDbKey));
    }

    public void setEntityDaoMap(Map<String, EntityDao> entityDaoMap) {
        this.entityDaoMap = entityDaoMap;
    }

    public void setCheckMigrateMap(Map<String, ICheckMigrate> checkMigrateMap) {
        this.checkMigrateMap = checkMigrateMap;
    }

    public Map<String, EntityDao> getEntityDaoMap() {
        return entityDaoMap;
    }

    public Map<String, ICheckMigrate> getCheckMigrateMap() {
        return checkMigrateMap;
    }
}
