package com.bdqn.controller;

import cn.itrip.common.DateUtil;
import cn.itrip.common.Dto;
import cn.itrip.common.DtoUtil;
import cn.itrip.dao.itripAreaDic.ItripAreaDicMapper;
import cn.itrip.dao.itripHotelRoom.ItripHotelRoomMapper;
import cn.itrip.dao.itripImage.ItripImageMapper;
import cn.itrip.dao.itripLabelDic.ItripLabelDicMapper;
import cn.itrip.pojo.ItripHotelRoom;
import cn.itrip.pojo.ItripHotelRoomVO;
import cn.itrip.pojo.SearchHotelRoomVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("/api")
public class BizController {

    @Resource
    ItripAreaDicMapper dao;

    @Resource
    ItripLabelDicMapper dao1;

    @Resource
    ItripHotelRoomMapper dao2;

    @Resource
    ItripImageMapper dao3;


    @RequestMapping("/hotelroom/getimg/{img}")
    @ResponseBody
    public  Dto GetImage(@PathVariable("img")String id)
    {
        return  DtoUtil.returnDataSuccess(dao3.getimagebyid(id));
    }

    @RequestMapping("/hotelroom/queryhotelroombyhotel")
    @ResponseBody
    public Dto<List<ItripHotelRoomVO>> GetRoom(@RequestBody SearchHotelRoomVO vo) throws Exception {

        List<List<ItripHotelRoomVO>> hotelRoomVOList=null;

       long hid=vo.getHotelId();
       Date sdate=vo.getStartDate();
       Date edate=vo.getEndDate();

       List<Date> dlist=DateUtil.getBetweenDates(sdate,edate);

        Map<String,Object> map=new HashMap<>();

        map.put("hid",hid);
        map.put("Dkey",dlist);
        List<ItripHotelRoomVO> list=dao2.GetRoom(map);

        //和前台做一个比较
        hotelRoomVOList = new ArrayList();
        for (ItripHotelRoomVO roomVO : list) {
            List<ItripHotelRoomVO> tempList = new ArrayList<ItripHotelRoomVO>();
            tempList.add(roomVO);
            hotelRoomVOList.add(tempList);
        }

        return  DtoUtil.returnDataSuccess(hotelRoomVOList);
    }

    @RequestMapping("/hotel/queryhotcity/{type}")
    @ResponseBody
    public Dto Getlist(@PathVariable("type")String type) throws Exception {
        return DtoUtil.returnDataSuccess(dao.ishot(type));
    }

    @RequestMapping("/hotel/querytradearea/{id}")
    @ResponseBody
    public Dto Getlist_1(@PathVariable("id")String id) throws Exception {
        return DtoUtil.returnDataSuccess(dao.selectByCity(id));
    }

    @RequestMapping("/hotel/queryhotelfeature")
    @ResponseBody
    public Dto Getlist_2() throws Exception {
        return DtoUtil.returnDataSuccess(dao1.lab());
    }

}
