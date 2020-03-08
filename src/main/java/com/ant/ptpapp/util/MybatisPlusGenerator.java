package com.ant.ptpapp.util;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @author Administrator
 * @title: MybatisPlusGenerator
 * @projectName springboot-demo
 * @description: TODO
 * @date 2019/9/16 0016下午 14:49
 */
public class MybatisPlusGenerator {
    private static MybatisPlusGenerator single = null;

    private MybatisPlusGenerator() {
        super();
    }

    private static MybatisPlusGenerator getSingle() {
        if (single == null) {
            single = new MybatisPlusGenerator();
        }
        return single;
    }

    public void autoGeneration() {
        GlobalConfig config = new GlobalConfig();
        String dbUrl = "jdbc:mysql://47.107.159.185:3306/ptp_app?characterEncoding=utf-8&serverTimezone=UTC&useSSL=false";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername("yc")
                .setPassword("yc")
                .setDriverName("com.mysql.cj.jdbc.Driver");
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setEntityLombokModel(false)
                .setInclude("ptp_report")
                // .setDbColumnUnderline(true)
                .setNaming(NamingStrategy.underline_to_camel);
        config.setActiveRecord(false)
                .setEnableCache(false)
                .setAuthor("yichen")
                //指定输出文件夹位置
//                .setOutputDir("F:\\pull\\ptp-app\\src\\main\\java")
                .setOutputDir("\\test\\")
                .setFileOverride(true)
                .setServiceName("%sService");
        new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent("com.ant.ptpapp")
                                .setController("controller")
                                .setEntity("entity")
                ).execute();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
//        MybatisPlusGenerator generator = MybatisPlusGenerator.getSingle();
//        generator.autoGeneration();
    }


}
