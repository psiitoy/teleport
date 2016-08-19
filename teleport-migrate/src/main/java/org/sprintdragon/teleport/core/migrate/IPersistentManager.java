package org.sprintdragon.teleport.core.migrate;

import org.sprintdragon.teleport.persistent.dao.EntityDao;

/**
 * Created by wangdi on 16-8-15.
 */
public interface IPersistentManager {

    EntityDao findEntityDaoByKey(String dbKey);

    ICheckMigrate findCheckMigrateByKey(String fromDbKey, String toDbKey);

}
