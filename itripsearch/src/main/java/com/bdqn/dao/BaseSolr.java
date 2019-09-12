package com.bdqn.dao;

import com.bdqn.entity.ItripHotelVO;
import com.bdqn.entity.Page;
import com.sun.org.apache.regexp.internal.REUtil;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.util.List;

public class BaseSolr<T> {
    String url="http://localhost:8080/solr/hotel-core/";
    HttpSolrClient client;
    public BaseSolr()
    {

        client=new HttpSolrClient(url);
        client.setParser(new XMLResponseParser());
        client.setConnectionTimeout(1000);
    }


    //封装DAO 方法，方便使用
    public  List<T> getList(SolrQuery solrQuery,int PageSize,Class clas_t) throws IOException, SolrServerException {

        /*  solrQuery.addFilterQuery("id:[1 to 2]");*/
        /*   solrQuery.addFilterQuery("hotelName:北京");*/
        solrQuery.setStart(0);
        solrQuery.setRows(PageSize);

        QueryResponse queryResponse = null;
        queryResponse = client.query(solrQuery);
        List<T> list = queryResponse.getBeans(clas_t);
        return  list;
    }

    //封装DAO 方法，方便使用
    public Page<T> GetList_page(SolrQuery solrQuery,int index, int PageSize, Class clas_t) throws IOException, SolrServerException {

        /*  solrQuery.addFilterQuery("id:[1 to 2]");*/
        /*   solrQuery.addFilterQuery("hotelName:北京");*/
        solrQuery.setStart((index-1)*PageSize);
        solrQuery.setRows(PageSize);

        QueryResponse queryResponse = null;
        queryResponse = client.query(solrQuery);


        SolrDocumentList count=queryResponse.getResults();

        List<T> list=(List<T>)queryResponse.getBeans(ItripHotelVO.class);

        Page<T> page = new Page<>(index,PageSize,new Long(count.getNumFound()).intValue());
        page.setRows(list);
        return  page;
    }
}
