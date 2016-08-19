package org.sprintdragon.teleport.core.migrate;

/**
 * Created by wangdi on 16-8-15.
 */
public interface ICheckMigrate<From, To> {

    To objTransform(From from);

    To getQuerySingleDomain(To to);

}
