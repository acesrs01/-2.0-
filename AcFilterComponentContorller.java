/*
 * Powered By [JHOP3.0-AutoCode]
 * Web Site: http://www.techstar.com
 * Since  2014-09-17 
 */

package com.techstar.jyy.platform.filter.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.jyy.platform.filter.entity.AcFilterComponent;
import com.techstar.jyy.platform.filter.service.AcFilterComponentService;
import com.techstar.jyy.platform.reactive.controller.BaseContorller;
import com.techstar.jyy.platform.util.AcfilterUdpSocketService;
import com.techstar.modules.jackson.mapper.JsonMapper;
import com.techstar.modules.springframework.data.jpa.domain.PageResponse;
import com.techstar.modules.springframework.data.jpa.domain.Results;

/**
 * @author JHOP3.0-AutoCode
 * @since   2014-09-17 16:05 
 */

/***
 * 元件参数信息-基于Spring MVC的控制层
 * @Date   2014-09-17 16:05 
 * @author JHOP-AutoCode
 */
@Controller
@RequestMapping("/acfilter/acFilterComponent")
public class AcFilterComponentContorller extends BaseContorller{
    
    private static Logger logger = LoggerFactory.getLogger(AcFilterComponentContorller.class);

	private static final JsonMapper JSONMAPPER = JsonMapper.nonEmptyMapper();

	@Autowired
	private AcfilterUdpSocketService acfilterUdpSocketService;
	
    @Autowired
    private AcFilterComponentService acFilterComponentService;

    /***
     * 列表数据查询
     * @Date   2014-09-17 16:05 
     * @author JHOP-AutoCode
     */
    //@RequiresPermissions(value = { "admin", "acFilterComponent:read" }, logical = Logical.OR)
    @RequestMapping("/search")
    public @ResponseBody Results search(@PageableDefaults(pageNumber = 0, value = 10) Pageable pageable, Specification<AcFilterComponent> spc,
    		 @RequestParam(value = "name", required = false) String name
    		 ,@RequestParam(value = "type", required = false) String type) {
	Page<AcFilterComponent> acfiltercomponent = acFilterComponentService.findAll(spc, pageable);
	String receivestr="";
	List<AcFilterComponent> acFilterComponentList=new ArrayList<AcFilterComponent>();
	if(type.equals("0")){
		acFilterComponentList=null;
	}else{
		try {
			receivestr=acfilterUdpSocketService.sendAcfilterOrder("0@" + this.getCurrentProjectUsernamePB()
					+ "@filter@tf_acfilter:" +name
					+ "@acfilter@read_rati1@null");
		} catch (IOException e) {
			e.printStackTrace();
		}
//	     if (receivestr.toUpperCase().indexOf("OK") > -1) {
		acFilterComponentList=acFilterComponentService.getAcFilterComponent(this.getCurrentProjectUsernamePB());
//      }else{
//   		filterStatusList=null;
//   	}
	}
	
	return new PageResponse<AcFilterComponent>(acFilterComponentList);
    }
    
   
}
