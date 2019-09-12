package com.bdqn.text;

import cn.itrip.pojo.ItripHotel;
import com.bdqn.entity.Hotel;
import com.bdqn.entity.ItripHotelVO;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;
import java.util.List;

public class SolrText {
    public static void main(String[] args) throws IOException, SolrServerException {
        //连接URL
        String url="http://localhost:8080/solr/hotel-core/";
        HttpSolrClient httpSolrClient=new HttpSolrClient(url);
        httpSolrClient.setParser(new XMLResponseParser());//设置响应解析器
        httpSolrClient.setConnectionTimeout(500);//建立连接的最长时间
        SolrQuery solrQuery=new SolrQuery();//新建查询
        solrQuery.setQuery("keyword:北京");
        QueryResponse queryResponse=httpSolrClient.query(solrQuery);

        List<ItripHotelVO> hotelList=queryResponse.getBeans(ItripHotelVO.class);
        for (ItripHotelVO hotel:hotelList){
            System.out.println(hotel.getHotelName());
        }
    }
}
