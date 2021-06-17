package com.vivo.dba.camapa;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 代码生成器使用步骤：
 * <p>
 * 1、导入依赖，如果不使用lombok，需设置 useLombok = false;
 *
 * <dependency>
 * <groupId>mysql</groupId>
 * <artifactId>mysql-connector-java</artifactId>
 * <version>5.1.48</version>
 * </dependency>
 *
 * <dependency>
 * <groupId>org.mybatis.spring.boot</groupId>
 * <artifactId>mybatis-spring-boot-starter</artifactId>
 * <version>2.1.4</version>
 * </dependency>
 *
 * <dependency>
 * <groupId>org.projectlombok</groupId>
 * <artifactId>lombok</artifactId>
 * </dependency>
 * <p>
 * <p>
 * 2、修改配置值
 * 如修改数据库连接地址、基本包所在路径（mainPath、moduleName）等。
 * <p>
 * 3、运行main方法
 */
public class MyCodeGenerator {

    //---------------------------配置值------------------------------//
    //库名
    private static final String dbName = "camapa";
    //表名
    private static final String tableName = "exp_msg_record";

    //数据库账号
    private static final String username = "root";
    //数据库密码
    private static final String password = "123456";
    //数据库url
    private static final String url = "jdbc:mysql://localhost:3306/" + dbName + "?useUnicode=true&useSSL=false&characterEncoding=utf8";

    //生成路径为： mainPath + moduleName 下的 controller/service/mapper
    private static final String mainPath = "D:\\devcode\\gitcode\\latest\\gitee-local\\code\\camapa\\src\\main";
    private static final String moduleName = "com\\vivo\\dba\\camapa";
    //Author
    private static final String author = "72084300";
    //是否使用lombok
    private static final boolean useLombok = true;
    //是否使用@Mapper注解。如果已经使用了@MapperScan可以设置为false
    private static final boolean useMapperNote = false;
    //是否使用MybatisPlus
    private static final boolean useMybatisPlus = true;
    //是否使用column constant
    private static final boolean useEntityColumnConstant = true;

    //---------------------------默认值------------------------------//
    //实体名
    private static final String entityName = underlineToCamel(tableName);

    private static final String basePackage = mainPath + "\\java\\" + moduleName;
    private static final String baseResource = mainPath + "\\resources";
    //是否覆盖原有文件
    private static final boolean isOverrideFile = true;

    //数据库驱动
    private static final String driver = "com.mysql.jdbc.Driver";
    //包路径：
    private static final String controllerPath = basePackage + "\\" + "controller";
    private static final String servicePath = basePackage + "\\" + "service";
    private static final String serviceImplPath = servicePath + "\\" + "impl";
    private static final String mapperPath = basePackage + "\\" + "mapper";
    private static final String entityPath = basePackage + "\\" + "entity";
    private static final String mapperXmlPath = baseResource + "\\" + "mapper";
    private static final String moduleNameInDot = moduleName.replaceAll("\\\\", ".");
    private static String primaryKey = "";
    private static String primaryJavaName = "";
    private static String primaryJavaType = "Object";
    private static List<Map> columnInfoList = null;


    public static void main(String[] args) throws Exception {
        tryConnect();
        makeSureExistPackage();
        generateEntity();
        generateMapper();
        generateMapperXml();
        generateService();
        generateServiceImpl();
        generateController();
    }

    private static void tryConnect() {
        columnInfoList = getColumnInfo();
        addJavaColumnName(columnInfoList);
    }

    private static void makeSureExistPackage() throws Exception {
        String basicDir = mainPath + "\\java\\";
        String[] dirArr = moduleName.split("\\\\");
        for (String dir : dirArr) {
            genDir(basicDir + dir);
            basicDir += (dir + "\\");
        }
        genDir(controllerPath);
        genDir(servicePath);
        genDir(serviceImplPath);
        genDir(mapperPath);
        genDir(mapperXmlPath);
        genDir(entityPath);
    }

    private static void generateMapperXml() {
        String pathname = mapperXmlPath + "\\" + entityName + "Mapper.xml";
        System.out.println("开始生成文件：" + pathname);
        File f = new File(pathname);
        if (f.exists() && !isOverrideFile) {
            return;
        }
        writeFile(pathname, useMybatisPlus ? getMapperPlusXmlDefaultContent() : getMapperXmlDefaultContent());

    }

    private static String getMapperPlusXmlDefaultContent() {
        String baseContent = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                        "<!DOCTYPE mapper\n" +
                        "        PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n" +
                        "        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
                        "\n" +
                        "<mapper namespace=\"$moduleNameInDot.mapper.$entityNameMapper\">\n" +
                        "\n" +
                        "    <!-- 通用查询映射结果 -->\n" +
                        "    <resultMap id=\"BaseResultMap\" type=\"$moduleNameInDot.entity.$entityName\">\n" +
                        "        %s\n" +
                        "    </resultMap>\n" +
                        "\n" +
                        "</mapper>",
                getBaseResultMap()
        );
        baseContent = baseContent.replace("$moduleNameInDot", moduleNameInDot);
        baseContent = baseContent.replace("$entityName", entityName);
        baseContent = baseContent.replace("$lowerEntityName", lowerFirst(entityName));
        baseContent = baseContent.replace("$primaryJavaType", primaryJavaType);
        baseContent = baseContent.replace("$primaryJavaName", primaryJavaName);
        baseContent = baseContent.replace("$tableName", tableName);
        baseContent = baseContent.replace("$primaryKey", primaryKey);
        return baseContent;
    }

    private static List<Map> getColumnInfo() {
        try {
            String sql = String.format("SELECT COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT, COLUMN_KEY FROM information_schema.COLUMNS WHERE table_schema = '%s' AND table_name = '%s'", dbName, tableName);
            Class.forName(driver);
            try (Connection con = DriverManager.getConnection(url, username, password)) {
                List query = new ArrayList();
                PreparedStatement pstmt = con.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    Map rstMap = new HashMap();
                    rstMap.put("COLUMN_NAME", rs.getString(1));
                    rstMap.put("DATA_TYPE", rs.getString(2));
                    rstMap.put("COLUMN_COMMENT", rs.getString(3));
                    rstMap.put("COLUMN_KEY", rs.getString(4));
                    query.add(rstMap);
                }
                return query;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("连接数据库异常：" + e.getMessage());
        }
    }

    private static void addJavaColumnName(List<Map> columnInfoList) {
        if (!columnInfoList.isEmpty()) {
            for (Map columnMap : columnInfoList) {
                columnMap.put("JAVA_COLUMN_NAME", underlineToCamel(columnMap.get("COLUMN_NAME").toString(), false));
                columnMap.put("JAVA_DATA_TYPE", dbTypeToJavaType(columnMap.get("DATA_TYPE").toString()));
                if (columnMap.get("COLUMN_KEY") != null && columnMap.get("COLUMN_KEY").toString().equalsIgnoreCase("PRI")) {
                    primaryKey = columnMap.get("COLUMN_NAME").toString();
                    primaryJavaName = underlineToCamel(primaryKey, false);
                    primaryJavaType = dbTypeToJavaType(columnMap.get("DATA_TYPE").toString());
                }
            }
        }
    }

    public static void genDir(String dirPath) throws Exception {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
            Thread.sleep(300);
            if (!dir.exists()) {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(500);
                    if (dir.exists()) {
                        break;
                    }
                }
            }
        }
    }

    public static void writeFile(String fileName, String content) {
        writeFile(fileName, content, false);
    }

    private static String getMapperXmlDefaultContent() {

        String baseContent = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                        "<!DOCTYPE mapper\n" +
                        "        PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n" +
                        "        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
                        "\n" +
                        "<mapper namespace=\"$moduleNameInDot.mapper.$entityNameMapper\">\n" +
                        "\n" +
                        "    <!-- 通用查询映射结果 -->\n" +
                        "    <resultMap id=\"BaseResultMap\" type=\"$moduleNameInDot.entity.$entityName\">\n" +
                        "        %s\n" +
                        "    </resultMap>\n" +
                        "\n" +
                        "    <insert id=\"insert\" parameterType=\"$moduleNameInDot.entity.$entityName\" useGeneratedKeys=\"true\" %s>\n" +
                        "        INSERT INTO $tableName (%s)\n" +
                        "        VALUES (%s)\n" +
                        "    </insert>\n" +
                        "\n" +
                        "    <update id=\"updateById\" parameterType=\"$moduleNameInDot.entity.$entityName\">\n" +
                        "        update $tableName set\n" +
                        "        %s\n" +
                        "        where $primaryKey = #{$primaryJavaName}\n" +
                        "    </update>\n" +
                        "\n" +
                        "    <select id=\"selectById\" resultMap=\"BaseResultMap\">\n" +
                        "        select\n" +
                        "        *\n" +
                        "        from $tableName\n" +
                        "        where $primaryKey = #{$primaryJavaName}\n" +
                        "    </select>\n" +
                        "\n" +
                        "    <delete id=\"deleteById\" parameterType=\"java.lang.$primaryJavaType\">\n" +
                        "        delete from $tableName\n" +
                        "        where $primaryKey = #{$primaryJavaName}\n" +
                        "    </delete>\n" +
                        "\n" +
                        "    <select id=\"selectList\" resultMap=\"BaseResultMap\">\n" +
                        "        select\n" +
                        "        *\n" +
                        "        from $tableName\n" +
                        "    </select>\n" +
                        "\n" +
                        "</mapper>",
                getBaseResultMap(),
                primaryKey.length() == 0 ? "" : ("keyProperty=\"" + primaryJavaName + "\""),
                getInsertDbColumns(),
                getInsertJavaColumns(),
                getUpdateColumns()
        );
        baseContent = baseContent.replace("$moduleNameInDot", moduleNameInDot);
        baseContent = baseContent.replace("$entityName", entityName);
        baseContent = baseContent.replace("$lowerEntityName", lowerFirst(entityName));
        baseContent = baseContent.replace("$primaryJavaType", primaryJavaType);
        baseContent = baseContent.replace("$primaryJavaName", primaryJavaName);
        baseContent = baseContent.replace("$tableName", tableName);
        baseContent = baseContent.replace("$primaryKey", primaryKey);
        return baseContent;
    }

    private static String getBaseResultMap() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<id column=\"%s\" property=\"%s\" />\n        ", primaryKey, underlineToCamel(primaryKey, false)));
        for (Map columnInfo : columnInfoList) {
            if (columnInfo.get("COLUMN_NAME").toString().equalsIgnoreCase(primaryKey)) {
                continue;
            }
            sb.append(String.format("<result column=\"%s\" property=\"%s\" />\n        ", columnInfo.get("COLUMN_NAME").toString(), columnInfo.get("JAVA_COLUMN_NAME").toString()));
        }
        return sb.toString().trim();
    }

    private static String underlineToCamel(String str, boolean firstToUpper) {
        String res = "";
        char[] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] + "").equals("_")) {
                res += (arr[i + 1] + "").toUpperCase();
                i++;
            } else {
                res += (arr[i] + "");
            }
        }

        if (firstToUpper) {
            return (res.substring(0, 1)).toUpperCase() + res.substring(1);
        }
        return res;
    }

    private static String dbTypeToJavaType(String dataType) {
        if (dataType.contains("int")) {
            if (dataType.equalsIgnoreCase("bigint")) {
                return "Long";
            }
            return "Integer";
        }

        if (dataType.contains("char")) {
            return "String";
        }

        if (dataType.contains("text")) {
            return "String";
        }

        if (dataType.equalsIgnoreCase("blob")) {
            return "byte[]";
        }

        if (dataType.equalsIgnoreCase("float")
                || dataType.equalsIgnoreCase("numeric")
                ) {
            return "Float";
        }

        if (dataType.equalsIgnoreCase("decimal")
                ) {
            return "BigDecimal";
        }

        if (dataType.contains("double")) {
            return "Double";
        }

        if (dataType.contains("date")
                || dataType.contains("time")
                || dataType.equalsIgnoreCase("year")
                ) {
            return "Date";
        }

        //找不到类型，就用Object..
        return "String";
    }

    /**
     * 追加内容写文件
     *
     * @param fileName
     * @param content
     */
    public static void writeFile(String fileName, String content, boolean append) {
        File f = new File(fileName);
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            if (!f.exists()) {
                f.createNewFile();
            }

            if (append) {
                fw = new FileWriter(f.getAbsoluteFile(), true); // true表示可以追加新内容
            } else {
                fw = new FileWriter(f.getAbsoluteFile()); // 表示不追加，每次覆盖之前的内容
            }
            bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getInsertDbColumns() {
        StringBuilder sb = new StringBuilder();
        for (Map columnInfo : columnInfoList) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            if (columnInfo.get("COLUMN_NAME").toString().equalsIgnoreCase(primaryKey)) {
                continue;
            }
            sb.append(columnInfo.get("COLUMN_NAME").toString());
        }
        return sb.toString();
    }

    private static String getInsertJavaColumns() {
        StringBuilder sb = new StringBuilder();
        for (Map columnInfo : columnInfoList) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            if (columnInfo.get("COLUMN_NAME").toString().equalsIgnoreCase(primaryKey)) {
                continue;
            }
            sb.append(String.format("#{%s}", columnInfo.get("JAVA_COLUMN_NAME").toString()));
        }
        return sb.toString();
    }

    private static String getUpdateColumns() {
        StringBuilder sb = new StringBuilder();
        for (Map columnInfo : columnInfoList) {
            if (columnInfo.get("COLUMN_NAME").toString().equalsIgnoreCase(primaryKey)) {
                continue;
            }
            sb.append(String.format("%s = #{%s},\n        ", columnInfo.get("COLUMN_NAME").toString(), columnInfo.get("JAVA_COLUMN_NAME").toString()));
        }
        return trimLast(sb.toString().trim());
    }

    private static String trimLast(String str) {
        return str.substring(0, str.length() - 1);
    }

    private static void generateMapper() {
        String pathname = mapperPath + "\\" + entityName + "Mapper.java";
        System.out.println("开始生成文件：" + pathname);
        File f = new File(pathname);
        if (f.exists() && !isOverrideFile) {
            return;
        }
        writeFile(pathname, useMybatisPlus ? getMapperPlusDefaultContent() : getMapperDefaultContent());
    }

    private static String getMapperPlusDefaultContent() {
        String baseContent = String.format("package $moduleNameInDot.mapper;\n" +
                        "\n" +
                        "%s" +
                        "%s" +
                        "import $moduleNameInDot.entity.$entityName;\n" +
                        "\n" +
                        "\n" +
                        "/**\n" +
                        " * @author %s\n" +
                        " * @since %s\n" +
                        " */\n" +
                        "%s" +
                        "public interface $entityNameMapper extends BaseMapper<$entityName> {\n" +
                        "\n" +
                        "}",
                useMapperNote ? "import org.apache.ibatis.annotations.Mapper;\n" : "",
                useMybatisPlus ? "import com.baomidou.mybatisplus.core.mapper.BaseMapper;\n" : "",
                author,
                today(),
                useMapperNote ? "@Mapper\n" : ""
        );
        baseContent = baseContent.replace("$moduleNameInDot", moduleNameInDot);
        baseContent = baseContent.replace("$entityName", entityName);
        baseContent = baseContent.replace("$lowerEntityName", lowerFirst(entityName));
        baseContent = baseContent.replace("$primaryJavaType", primaryJavaType);
        baseContent = baseContent.replace("$primaryJavaName", primaryJavaName);
        return baseContent;
    }

    private static String getMapperDefaultContent() {
        String baseContent = String.format("package $moduleNameInDot.mapper;\n" +
                        "\n" +
                        "%s" +
                        "import java.util.List;\n" +
                        "import $moduleNameInDot.entity.$entityName;\n" +
                        "\n" +
                        "\n" +
                        "/**\n" +
                        " * @author %s\n" +
                        " * @since %s\n" +
                        " */\n" +
                        "%s" +
                        "public interface $entityNameMapper {\n" +
                        "\n" +
                        "    void insert($entityName $lowerEntityName);\n" +
                        "\n" +
                        "    void deleteById($primaryJavaType $primaryJavaName);\n" +
                        "\n" +
                        "    void updateById($entityName $lowerEntityName);\n" +
                        "\n" +
                        "    $entityName selectById($primaryJavaType $primaryJavaName);\n" +
                        "\n" +
                        "    List<$entityName> selectList();\n" +
                        "\n" +
                        "}",
                useMapperNote ? "import org.apache.ibatis.annotations.Mapper;\n" : "",
                author,
                today(),
                useMapperNote ? "@Mapper\n" : ""
        );
        baseContent = baseContent.replace("$moduleNameInDot", moduleNameInDot);
        baseContent = baseContent.replace("$entityName", entityName);
        baseContent = baseContent.replace("$lowerEntityName", lowerFirst(entityName));
        baseContent = baseContent.replace("$primaryJavaType", primaryJavaType);
        baseContent = baseContent.replace("$primaryJavaName", primaryJavaName);
        return baseContent;
    }

    private static String today() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    private static String lowerFirst(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    private static void generateEntity() {
        String pathname = entityPath + "\\" + entityName + ".java";
        System.out.println("开始生成文件：" + pathname);
        File f = new File(pathname);
        if (f.exists() && !isOverrideFile) {
            return;
        }
        writeFile(pathname, getEntityDefaultContent());
    }

    private static String getEntityDefaultContent() {
        String columnInfo = columnInfoListToStr(columnInfoList);
        String baseContent = String.format("package $moduleNameInDot.entity;\n" +
                        "\n" +
                        "%s" +
                        "\n" +
                        "/**\n" +
                        " * @author %s\n" +
                        " * @since %s\n" +
                        " */\n" +
                        "%s" +
                        "%s" +
                        "public class $entityName {\n" +
                        "\n" +
                        "%s" +
                        "\n" +
                        "%s" +
                        "}\n",
                getImport(columnInfo),
                author,
                today(),
                useLombok ? "@Data\n" : "",
                useMybatisPlus ? "@TableName(\"" + tableName + "\")\n" : "",
                columnInfo,
                useEntityColumnConstant ? entityColumnConstant() : ""
        );
        baseContent = baseContent.replace("$moduleNameInDot", moduleNameInDot);
        baseContent = baseContent.replace("$entityName", entityName);
        return baseContent;
    }

    private static String entityColumnConstant() {
        StringBuilder sb = new StringBuilder();
        for (Map columnInfo : columnInfoList) {
            sb.append(String.format("    private static final String %s = \"%s\";\n\n",
                    columnInfo.get("COLUMN_NAME").toString().toUpperCase(),
                    columnInfo.get("COLUMN_NAME")));
        }
        return sb.toString();
    }

    private static String columnInfoListToStr(List<Map> columnInfoList) {
        StringBuilder sb = new StringBuilder();
        for (Map columnInfo : columnInfoList) {
            sb.append(String.format("    %s%sprivate %s %s;\n",
                    conmentStr(columnInfo.get("COLUMN_COMMENT")),
                    addTableId(columnInfo.get("JAVA_COLUMN_NAME")),
                    columnInfo.get("JAVA_DATA_TYPE").toString(),
                    columnInfo.get("JAVA_COLUMN_NAME")));
        }

        /**
         *     public void setId(Integer id) {
         *         this.id = id;
         *     }
         *
         *     public Integer getId() {
         *         return this.id;
         *     }
         */
        if (!useLombok) {
            for (Map columnInfo : columnInfoList) {
                String javaColumnName = columnInfo.get("JAVA_COLUMN_NAME").toString();
                String javaDataType = columnInfo.get("JAVA_DATA_TYPE").toString();
                String setterGetter = String.format("\n    public void set%s(%s %s) {\n" +
                                "        this.%s = %s;\n" +
                                "    }\n" +
                                "\n" +
                                "    public %s get%s() {\n" +
                                "        return this.%s;\n" +
                                "    }\n",
                        underlineToCamel(javaColumnName),
                        javaDataType,
                        javaColumnName,
                        javaColumnName,
                        javaColumnName,
                        javaDataType,
                        underlineToCamel(javaColumnName),
                        javaColumnName
                );
                sb.append(setterGetter);
            }
        }
        return sb.toString();
    }

    private static Object addTableId(Object javaColumnName) {
        if (javaColumnName.equals(primaryJavaName) && useMybatisPlus) {
            return "@TableId(value = \"" + primaryKey + "\", type = IdType.AUTO)\n    ";
        }
        return "";
    }

    private static Object getImport(String columnInfo) {
        StringBuilder sb = new StringBuilder();
        if (useLombok) {
            sb.append("import lombok.Data;\n");
        }
        if (useMybatisPlus) {
            sb.append("import com.baomidou.mybatisplus.annotation.IdType;\n" +
                    "import com.baomidou.mybatisplus.annotation.TableId;\n" +
                    "import com.baomidou.mybatisplus.annotation.TableName;\n");
        }
        if (columnInfo.contains("private Date ")) {
            sb.append("import java.util.Date;\n");
        }
        if (columnInfo.contains("private BigDecimal ")) {
            sb.append("import java.math.BigDecimal;\n");
        }
        return sb.toString();
    }

    private static String conmentStr(Object column_comment) {
        if (column_comment != null && column_comment.toString().length() > 0) {
            return String.format("/**\n" +
                    "     * %s\n" +
                    "     */\n    ", column_comment.toString());
        }
        return "\n    ";
    }

    private static String underlineToCamel(String tableName) {
        return underlineToCamel(tableName, true);
    }

    private static void generateServiceImpl() {
        String pathname = serviceImplPath + "\\" + entityName + "ServiceImpl.java";
        System.out.println("开始生成文件：" + pathname);
        File f = new File(pathname);
        if (f.exists() && !isOverrideFile) {
            return;
        }
        writeFile(pathname, useMybatisPlus ? getServiceImplPlusXmlDefaultContent() : getServiceImplDefaultContent());
    }

    private static String getServiceImplPlusXmlDefaultContent() {
        String baseContent = String.format("package $moduleNameInDot.service.impl;\n" +
                        "\n" +
                        "import org.springframework.stereotype.Service;\n" +
                        "import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\n" +
                        "import $moduleNameInDot.service.$entityNameService;\n" +
                        "import $moduleNameInDot.mapper.$entityNameMapper;\n" +
                        "import $moduleNameInDot.entity.$entityName;\n" +
                        "\n" +
                        "/**\n" +
                        " * @author %s\n" +
                        " * @since %s\n" +
                        " */\n" +
                        "@Service\n" +
                        "public class $entityNameServiceImpl extends ServiceImpl<$entityNameMapper, $entityName> implements $entityNameService {\n" +
                        "\n" +
                        "}\n",
                author,
                today()
        );
        baseContent = baseContent.replace("$moduleNameInDot", moduleNameInDot);
        baseContent = baseContent.replace("$entityName", entityName);
        baseContent = baseContent.replace("$lowerEntityName", lowerFirst(entityName));
        baseContent = baseContent.replace("$primaryJavaType", primaryJavaType);
        baseContent = baseContent.replace("$primaryJavaName", primaryJavaName);
        return baseContent;
    }

    private static String getServiceImplDefaultContent() {
        String baseContent = String.format("package $moduleNameInDot.service.impl;\n" +
                        "\n" +
                        "import org.springframework.stereotype.Service;\n" +
                        "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "import java.util.List;\n" +
                        "import $moduleNameInDot.service.$entityNameService;\n" +
                        "import $moduleNameInDot.mapper.$entityNameMapper;\n" +
                        "import $moduleNameInDot.entity.$entityName;\n" +
                        "\n" +
                        "/**\n" +
                        " * @author %s\n" +
                        " * @since %s\n" +
                        " */\n" +
                        "@Service\n" +
                        "public class $entityNameServiceImpl implements $entityNameService {\n" +
                        "\n" +
                        "    @Autowired\n" +
                        "    private $entityNameMapper $lowerEntityNameMapper;\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public void save($entityName $lowerEntityName) {\n" +
                        "        $lowerEntityNameMapper.insert($lowerEntityName);\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public void deleteById($primaryJavaType $primaryJavaName) {\n" +
                        "        $lowerEntityNameMapper.deleteById($primaryJavaName);\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public void updateById($entityName $lowerEntityName) {\n" +
                        "        $lowerEntityNameMapper.updateById($lowerEntityName);\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public $entityName selectById($primaryJavaType $primaryJavaName) {\n" +
                        "        return $lowerEntityNameMapper.selectById($primaryJavaName);\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public List<$entityName> list() {\n" +
                        "        return $lowerEntityNameMapper.selectList();\n" +
                        "    }\n" +
                        "}\n",
                author,
                today()
        );
        baseContent = baseContent.replace("$moduleNameInDot", moduleNameInDot);
        baseContent = baseContent.replace("$entityName", entityName);
        baseContent = baseContent.replace("$lowerEntityName", lowerFirst(entityName));
        baseContent = baseContent.replace("$primaryJavaType", primaryJavaType);
        baseContent = baseContent.replace("$primaryJavaName", primaryJavaName);
        return baseContent;
    }

    private static void generateService() {
        String pathname = servicePath + "\\" + entityName + "Service.java";
        System.out.println("开始生成文件：" + pathname);
        File f = new File(pathname);
        if (f.exists() && !isOverrideFile) {
            return;
        }
        writeFile(pathname, useMybatisPlus ? getServicePlusXmlDefaultContent() : getServiceDefaultContent());
    }

    private static String getServicePlusXmlDefaultContent() {
        String baseContent = String.format("package $moduleNameInDot.service;\n" +
                        "\n" +
                        "import com.baomidou.mybatisplus.extension.service.IService;\n" +
                        "import $moduleNameInDot.entity.$entityName;\n" +
                        "/**\n" +
                        " * @author %s\n" +
                        " * @since %s\n" +
                        " */\n" +
                        "public interface $entityNameService extends IService<$entityName> {\n" +
                        "\n" +
                        "}\n",
                author,
                today()
        );
        baseContent = baseContent.replace("$moduleNameInDot", moduleNameInDot);
        baseContent = baseContent.replace("$entityName", entityName);
        baseContent = baseContent.replace("$lowerEntityName", lowerFirst(entityName));
        baseContent = baseContent.replace("$primaryJavaType", primaryJavaType);
        baseContent = baseContent.replace("$primaryJavaName", primaryJavaName);
        return baseContent;
    }

    private static String getServiceDefaultContent() {
        String baseContent = String.format("package $moduleNameInDot.service;\n" +
                        "\n" +
                        "import java.util.List;\n" +
                        "import $moduleNameInDot.entity.$entityName;\n" +
                        "/**\n" +
                        " * @author %s\n" +
                        " * @since %s\n" +
                        " */\n" +
                        "public interface $entityNameService {\n" +
                        "\n" +
                        "    void save($entityName $lowerEntityName);\n" +
                        "\n" +
                        "    void deleteById($primaryJavaType $primaryJavaName);\n" +
                        "\n" +
                        "    void updateById($entityName $lowerEntityName);\n" +
                        "\n" +
                        "    $entityName selectById($primaryJavaType $primaryJavaName);\n" +
                        "\n" +
                        "    List<$entityName> list();\n" +
                        "}\n",
                author,
                today()
        );
        baseContent = baseContent.replace("$moduleNameInDot", moduleNameInDot);
        baseContent = baseContent.replace("$entityName", entityName);
        baseContent = baseContent.replace("$lowerEntityName", lowerFirst(entityName));
        baseContent = baseContent.replace("$primaryJavaType", primaryJavaType);
        baseContent = baseContent.replace("$primaryJavaName", primaryJavaName);
        return baseContent;
    }

    private static void generateController() {
        String pathname = controllerPath + "\\" + entityName + "Controller.java";
        System.out.println("开始生成文件：" + pathname);
        File f = new File(pathname);
        if (f.exists() && !isOverrideFile) {
            return;
        }
        writeFile(pathname, getControllerDefaultContent());
    }

    private static String getControllerDefaultContent() {
        String baseContent = String.format("package $moduleNameInDot.controller;\n" +
                        "\n" +
                        "import $moduleNameInDot.entity.$entityName;\n" +
                        "import $moduleNameInDot.service.$entityNameService;\n" +
                        "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "import org.springframework.web.bind.annotation.*;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "/**\n" +
                        " * @author %s\n" +
                        " * @since %s\n" +
                        " */\n" +
                        "@RestController\n" +
                        "@RequestMapping(\"/%s/$lowerEntityName/v1\")\n" +
                        "public class $entityNameController {\n" +
                        "\n" +
                        "    @Autowired\n" +
                        "    private $entityNameService $lowerEntityNameService;\n" +
                        "\n" +
                        "    @PostMapping(\"/save\")\n" +
                        "    public Object save(@RequestBody $entityName $lowerEntityName) {\n" +
                        "        $lowerEntityNameService.save($lowerEntityName);\n" +
                        "        return \"ok\";\n" +
                        "    }\n" +
                        "\n" +
                        "    @GetMapping(\"/list\")\n" +
                        "    public Object list() {\n" +
                        "        List<$entityName> list = $lowerEntityNameService.list(%s);\n" +
                        "        return list;\n" +
                        "    }\n" +
                        "\n" +
                        "}\n",
                author,
                today(),
                moduleName.replaceFirst(".+\\\\(\\w+)", "$1"),
                useMybatisPlus ? "null" : ""
        );
        baseContent = baseContent.replace("$moduleNameInDot", moduleNameInDot);
        baseContent = baseContent.replace("$entityName", entityName);
        baseContent = baseContent.replace("$lowerEntityName", lowerFirst(entityName));
        baseContent = baseContent.replace("$primaryJavaType", primaryJavaType);
        baseContent = baseContent.replace("$primaryJavaName", primaryJavaName);
        return baseContent;
    }

}
