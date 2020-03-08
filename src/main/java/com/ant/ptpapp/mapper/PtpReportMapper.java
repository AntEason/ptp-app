package com.ant.ptpapp.mapper;

import com.ant.ptpapp.entity.PtpByDeviceInfo;
import com.ant.ptpapp.entity.PtpReport;
import com.ant.ptpapp.entity.PtpTrim;
import com.ant.ptpapp.entity.PtpUserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yichen
 * @since 2020-02-29
 */
public interface PtpReportMapper extends BaseMapper<PtpReport> {

    IPage<PtpReport> selectPageVo(Page<?> page, @Param("userPhone") String userPhone,
                                  @Param("userId") String userId,
                                  @Param("startTime")String startTime,
                                  @Param("endTime")String endTime,
                                  @Param("condition")String condition);

    IPage<PtpByDeviceInfo> findByDeviceInfo(Page<?> page,
                                        @Param("deviceId")String deviceId,
                                        @Param("startTime")String startTime,
                                        @Param("endTime")String endTime,
                                        @Param("condition")String condition);

    IPage<PtpReport> findByTripCodePage(Page<?> page,@Param("userId") String userId);

    List<PtpReport> findByTripCode(@Param("userId")String userId,@Param("tripCode")String tripCode);

    List<PtpTrim> findByTripCodeInReport(@Param("tripCode")String tripCode);
}
