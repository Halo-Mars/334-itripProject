package com.bdqn.controller;

import cn.itrip.common.*;
import cn.itrip.dao.itripUser.ItripUserMapper;

import cn.itrip.pojo.ItripUser;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bdqn.biz.SentSSM;
import com.bdqn.biz.TokenBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@Controller
@RequestMapping("/api")
@Api(value="api",description = "用户模块")
public class UserController {

    @Resource
    ItripUserMapper itripUserMapper;

    @Resource
    TokenBiz biz;

    @Resource
    JredisApi redisAPI;

    @Resource
    SentSSM sentSSM;

    @Resource
    ItripUserMapper userdao;

    //验证
    @RequestMapping(value="/validatephone")
    @ResponseBody
    public Dto validatwphone(String user,String code){
        try {
            //去redis中查找数据
            String result = redisAPI.getRedis("Code:"+user);
            if(result.equals(code)){
                //根据手机号查询实体，然后再update
                /*ItripUser tt = null;

                tt = itripUserMapper.getUserE(user);*/
                itripUserMapper.UpdateByid(user);

                return DtoUtil.returnDataSuccess("激活成功！！！");
            }
        }catch (Exception e) {
            return DtoUtil.returnFail("激活失败！","10000");
        }

        return DtoUtil.returnFail("激活失败！","10000");

    }

    @RequestMapping(value="/registerbyphone",method = RequestMethod.POST)
    @ResponseBody

    public Dto registerbyphone(@RequestBody ItripUser itripUser) throws Exception {
        try{
            //为手机号发送验证码
            long number=(int)(Math.random()*9000+1000);
            //把随机4位数字发送给手机，把这个四位验证码存入到rides中去
            sentSSM.SetPhone(itripUser.getUserCode(),number+"");

            //存入到rides中
            redisAPI.SetRedis("Code:"+itripUser.getUserCode(),number+"",60);

            //把实体类存入到数据库中去
            ItripUser user=new ItripUser();
            user.setUserCode(itripUser.getUserCode());
            user.setUserName(itripUser.getUserName());
            user.setUserPassword(MD5.getMd5(itripUser.getUserPassword(),32));
          //设置激活状态
            user.setActivated(0);


           int result=userdao.insertItripUser2(user);
           if (result>0){

               return DtoUtil.returnDataSuccess(user);
           }

        }catch (Exception e){
                return  DtoUtil.returnFail("注册失败","1000");
        }
            return  DtoUtil.returnFail("注册失败","1000");

    }



    @RequestMapping(value="/dologin",method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            //paramType = "query"-->如果是get请求则为query，如果是post请求则为form,required = true -->值必须传,value = "提示",name = "映射到哪个字段",defaultValue ="如果没写映射字段，给一个默认值"
            @ApiImplicitParam(paramType = "form",required = true,value = "用户名",name = "name"),
            @ApiImplicitParam(paramType = "form",required = true,value = "密码",name = "password")
    })
    public Dto getList(HttpServletRequest request, String name, String password){
        ItripUser itripUser = null;
        try {
            //判断数据库中是否存在,密码采用了MD5算法进行加密
            itripUser = itripUserMapper.iflogin(name,MD5.getMd5(password,32));
            //如果存在，将该标识（手机端登录还是pc端登录）存入到redis中
            if(itripUser != null){
                //生成一个token
                String token = biz.generateToken(request.getHeader("User-Agent"),itripUser);
                redisAPI.SetRedis(token,JSONObject.toJSONString(itripUser),7200);

                //将当前时间与过期时间返回到前台
                ItripTokenVO tokenVO = new ItripTokenVO(token,Calendar.getInstance().getTimeInMillis()+7200,Calendar.getInstance().getTimeInMillis());
                return DtoUtil.returnDataSuccess(tokenVO);
            }
            /*itripUser = itripUserMapper.getItripUserById(new Long(12));*/


        } catch (Exception e) {

        }
        return DtoUtil.returnFail("登录失败","1000");
    }


}
