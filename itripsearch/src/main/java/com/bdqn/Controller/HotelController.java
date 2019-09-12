package com.bdqn.Controller;

import cn.itrip.common.Dto;
import cn.itrip.common.DtoUtil;
import com.bdqn.dao.BaseSolr;
import com.bdqn.entity.ItripHotelVO;
import com.bdqn.entity.Page;
import com.bdqn.entity.SearchHotCityVO;
import com.bdqn.entity.SearchHotelVO;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api")
public class HotelController {


    @RequestMapping(value = "/hotellist/searchItripHotelListByHotCity",method=RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public Dto GetSearchHotel(@RequestBody SearchHotCityVO searchHotelVO) throws IOException,Exception {

        BaseSolr<ItripHotelVO> dao=new BaseSolr();

        SolrQuery solrQuery=new SolrQuery("*:*");

        solrQuery.addFilterQuery("cityId:"+searchHotelVO.getCityId());

        List<ItripHotelVO> list=dao.getList(solrQuery,searchHotelVO.getCount(),ItripHotelVO.class);

        return DtoUtil.returnDataSuccess(list);
    }


    @RequestMapping(value = "hotellist/searchItripHotelPage",method=RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public Dto GetSearchHotel_page(@RequestBody SearchHotelVO vo) throws IOException,Exception {


        if (vo.getPageNo()==null)
        {
            vo.setPageNo(1);
        }
        vo.setPageSize(5);

        BaseSolr<ItripHotelVO> dao=new BaseSolr();

        SolrQuery solrQuery=new SolrQuery("*:*");

        if(vo.getDestination()!=null)
        {
            solrQuery.addFilterQuery("destination:"+vo.getDestination());
        }

        if(vo.getKeywords()!=null)
        {
            solrQuery.addFilterQuery("keyword:"+vo.getKeywords());
        }

        if (vo.getHotelLevel()!=null)
        {
            solrQuery.addFilterQuery("hotelLevel:"+vo.getHotelLevel());
        }
        if (vo.getFeatureIds()!=null&&vo.getFeatureIds()!="")
        {
            String [] str=vo.getFeatureIds().split(",");
            String stt="";
            for(int i=0;i<str.length;i++)
            {
                if(i==0)
                {
                    stt+="featureIds:*,"+str[i]+",*";
                }
                else {
                    stt+=" or featureIds:*,"+str[i]+",*";
                }
            }
            solrQuery.addFilterQuery(stt);
        }
        if(vo.getTradeAreaIds()!=null&&vo.getTradeAreaIds()!="")
        {
            String [] str=vo.getTradeAreaIds().split(",");
            String stt="";
            for(int i=0;i<str.length;i++)
            {
                if(i==0)
                {
                    stt+="tradingAreaIds:*,"+str[i]+",*";
                }
                else {
                    stt+=" or tradingAreaIds:*,"+str[i]+",*";
                }
            }
            solrQuery.addFilterQuery(stt);

        }

        if(vo.getMinPrice()!=null)
        {
            solrQuery.addFilterQuery("minPrice:["+ vo.getMinPrice()+" TO * ]");
        }
        if(vo.getMaxPrice()!=null)
        {
            solrQuery.addFilterQuery("maxPrice:[* TO "+vo.getMaxPrice()+" ]");
        }

        if (vo.getAscSort()!=null&&vo.getAscSort()!="")
        {
            solrQuery.setSort(vo.getAscSort(),SolrQuery.ORDER.asc);
        }
        if (vo.getDescSort()!=null)
        {
            solrQuery.setSort(vo.getDescSort(),SolrQuery.ORDER.desc);
        }


        Page<ItripHotelVO> list=dao.GetList_page(solrQuery,vo.getPageNo(),vo.getPageSize(),ItripHotelVO.class);

        return  DtoUtil.returnDataSuccess(list);

    }

    @RequestMapping("/text")
    @ResponseBody
    public String GetList()
    {
        return "字符串";
    }

}
