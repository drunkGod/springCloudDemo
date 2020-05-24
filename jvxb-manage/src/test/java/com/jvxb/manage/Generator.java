package com.jvxb.manage;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author jvxb
 * @function Generate code
 */
public class Generator {

    private static final String OUTPUT_DIR = "D:\\workspace-fbs\\microservice\\jvxb-manage\\src\\main\\java";
    private static final String PACKAGE_NAME = "com.jvxb.manage.livable";
    private static final String AUTHOR = "jvxb";

    private static final String DB_URL = "jdbc:mysql://localhost:3306/bg_manage?useUnicode=true&serverTimezone=UTC&characterEncoding=utf8&useSSL=false";
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_USER_NAME = "root";
    private static final String DB_PASSWORD = "123456";
    private static final String TABLE_PREFIX = "";

    private static final String ENTITY_NAME = "entity";
    private static final String MAPPER_NAME = "mapper";
    private static final String MAPPER_XML_NAME = "mapper.xml";
    private static final String SERVICE_NAME = "service";
    private static final String SERVICE_IMPL_NAME = "service.impl";
    private static final String CONTROLLER_NAME = "controller";

    public static void main(String[] args) {
        //auth -> UserService, 设置成true: auth -> IUserService
        boolean serviceNameStartWithI = false;
        generateByTables(serviceNameStartWithI, "user","beauty");
    }

    /**
     * @param serviceNameStartWithI
     * @param tableNames            表名
     */
    private static void generateByTables(boolean serviceNameStartWithI, String... tableNames) {
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(false).setEnableCache(false) // XML 二级缓存
                .setBaseResultMap(true).setBaseColumnList(true).setAuthor(AUTHOR).setOutputDir(OUTPUT_DIR)
                .setFileOverride(true)
                .setOpen(true)
                .setSwagger2(true);
        if (!serviceNameStartWithI) {
            config.setServiceName("%sService");
        }

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL).setUrl(DB_URL)
                .setUsername(DB_USER_NAME).setPassword(DB_PASSWORD).setDriverName(DB_DRIVER);


        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(true).setEntityLombokModel(true).setNaming(NamingStrategy.underline_to_camel)
                .setTablePrefix(TABLE_PREFIX)// 此处可以修改为您的表前缀
                .setEntityColumnConstant(true) // 生成字段常量
                .setInclude(tableNames)// 修改替换成你需要的表名，多个表名传数组
                .setRestControllerStyle(true);

        new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig).setStrategy(strategyConfig)
                .setPackageInfo(new PackageConfig()
                        .setParent(PACKAGE_NAME)
                        .setController(CONTROLLER_NAME)
                        .setEntity(ENTITY_NAME)
                        .setMapper(MAPPER_NAME)
                        .setService(SERVICE_NAME)
                        .setServiceImpl(SERVICE_IMPL_NAME)
                        .setXml(MAPPER_XML_NAME))
                .execute();
    }

    private void deleteEntity(String... entityNames) throws IOException {
        for (String entity : entityNames) {
            String dir = joinPath(OUTPUT_DIR, PACKAGE_NAME);
            // 删除controller
            FileUtils.forceDeleteOnExit(new File(dir + "\\" + CONTROLLER_NAME + "\\" + entity + "Controller.java"));
            // 删除serviceImpl
            FileUtils.forceDeleteOnExit(new File(dir + "\\service\\impl\\" + entity + "ServiceImpl.java"));
            // 删除service
            FileUtils.forceDeleteOnExit(new File(dir + "\\service\\" + entity + "Service.java"));
            // 删除mapper
            FileUtils.forceDeleteOnExit(new File(dir + "\\mapper\\" + entity + "Mapper.java"));
            // 删除mapper.xml
            FileUtils.forceDeleteOnExit(new File(dir + "\\mapper\\xml\\" + entity + "Mapper.xml"));
            // 删除entity
            FileUtils.forceDeleteOnExit(new File(dir + "\\" + ENTITY_NAME + "\\" + entity + ".java"));
            System.out.println("删除完成！");
        }
    }

    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isEmpty(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
        return parentDir + packageName;
    }

}