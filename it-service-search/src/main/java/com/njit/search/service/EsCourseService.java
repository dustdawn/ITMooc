package com.njit.search.service;

import com.njit.framework.domain.course.CoursePub;
import com.njit.framework.domain.course.TeachplanMediaPub;
import com.njit.framework.domain.search.CourseSearchParam;
import com.njit.framework.model.response.CommonCode;
import com.njit.framework.model.response.QueryResponseResult;
import com.njit.framework.model.response.QueryResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dustdawn
 * @date 2020/2/23 15:15
 */
@Service
public class EsCourseService {
    @Value("${itmooc.course.index}")
    private String index;
    @Value("${itmooc.course.type}")
    private String type;
    @Value("${itmooc.course.source_field}")
    private  String sourse_field;

    @Value("${itmooc.media.index}")
    private String media_index;
    @Value("${itmooc.media.type}")
    private String media_type;
    @Value("${itmooc.media.source_field}")
    private  String media_sourse_field;

    @Autowired
    RestHighLevelClient restHighLevelClient;
    /**
     * 课程搜索
     * @param page
     * @param size
     * @param param
     * @return
     */
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam param) {
        if (param == null) {
            param = new CourseSearchParam();
        }
        //创造搜索请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        //设置搜索类型
        searchRequest.types(type);

        //设置过滤源字段
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String[] sourse_field_array = sourse_field.split(",");
        //显示和不显示
        searchSourceBuilder.fetchSource(sourse_field_array, new String[]{});



        //创建Boolean查询对象, 组装查询结构
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //搜索条件
        //1.根据关键字搜索
        if (StringUtils.isNotEmpty(param.getKeyword())) {
            //匹配关键字
            //匹配占比 3*0.7=2.1，向上取证为2个次至少匹配
            //提升另一个字段的Boost值，权重10倍
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(param.getKeyword(), "name", "description", "teachplan")
                    .minimumShouldMatch("70%")
                    .field("name", 10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }

        //过滤器
        //2.根据分类查询
        if (StringUtils.isNotEmpty(param.getMt())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("mt", param.getMt()));
        }
        if (StringUtils.isNotEmpty(param.getSt())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("st", param.getSt()));
        }
        //等级
        if (StringUtils.isNotEmpty(param.getGrade())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("grade", param.getGrade()));
        }


        //设置boolean查询对象到searchSourceBuilder
        searchSourceBuilder.query(boolQueryBuilder);

        //设置分页参数
        if (page <= 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 12;
        }
        //起始记录下标
        int from = (page-1)*size;

        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);

        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        //设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);

        QueryResult<CoursePub> queryResult = new QueryResult<>();
        List<CoursePub> list = new ArrayList<>();
        //执行搜索
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            //获取响应结果
            SearchHits hits = searchResponse.getHits();
            //匹配的总记录数
            long totalHits = hits.totalHits;


            queryResult.setTotal(totalHits);
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit sh : searchHits) {
                CoursePub coursePub = new CoursePub();
                //源文档
                Map<String, Object> sourceAsMap = sh.getSourceAsMap();
                //取出id
                String id = (String) sourceAsMap.get("id");
                coursePub.setId(id);
                //取出name
                String name = (String) sourceAsMap.get("name");
                //取出高亮字段name
                Map<String, HighlightField> highlightFields = sh.getHighlightFields();
                if (highlightFields != null) {
                    HighlightField highlightFieldName = highlightFields.get("name");
                    if (highlightFieldName != null) {
                        Text[] fragments = highlightFieldName.fragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        for (Text text : fragments) {
                            stringBuffer.append(text);
                        }
                        name = stringBuffer.toString();
                    }
                }
                coursePub.setName(name);
                //图片
                String pic = (String) sourceAsMap.get("pic");
                coursePub.setPic(pic);

                list.add(coursePub);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        queryResult.setList(list);
        QueryResponseResult<CoursePub> queryResponseResult = new QueryResponseResult<CoursePub>(CommonCode.SUCCESS, queryResult);


        return queryResponseResult;
    }

    /**
     * 使用ES的客户端向ES请求查询索引信息
     * @param courseId
     * @return
     */
    public Map<String, CoursePub> getall(String courseId) {
        //定义一个搜索请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        //指定type
        searchRequest.types(type);

        //定义SearchSourBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置使用termQuery
        searchSourceBuilder.query(QueryBuilders.termQuery("id", courseId));
        //过滤源字段，不用设置源字段，取出所有字段
        //searchSourceBuilder.fetchSource()
        searchRequest.source(searchSourceBuilder);
        //最终要返回的课程信息
        CoursePub coursePub = new CoursePub();
        Map<String, CoursePub> map = new HashMap<>();
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            SearchHits hits = searchResponse.getHits();
            //取出一条查询的课程
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit searchHit : searchHits) {
                //获取源文档的内容
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                String id = (String) sourceAsMap.get("id");
                String name = (String) sourceAsMap.get("name");
                String grade = (String) sourceAsMap.get("grade");
                String pic = (String) sourceAsMap.get("pic");
                String description = (String) sourceAsMap.get("description");
                String teachplan = (String) sourceAsMap.get("teachplan");

                coursePub.setId(id);
                coursePub.setName(name);
                coursePub.setGrade(grade);
                coursePub.setPic(pic);
                coursePub.setDescription(description);
                coursePub.setTeachplan(teachplan);

                map.put(courseId, coursePub);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     *  根据多个课程计划id查询媒资信息
     * @param teachplanIds
     * @return
     */
    public QueryResponseResult<TeachplanMediaPub> getmedia(String[] teachplanIds) {
        //定义一个搜索请求对象
        SearchRequest searchRequest = new SearchRequest(media_index);
        //指定type
        searchRequest.types(media_type);

        //定义SearchSourBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置使用termQuery根据多个id查询
        searchSourceBuilder.query(QueryBuilders.termsQuery("teachplan_id", teachplanIds));
        //过滤源字段
        String[] includes = media_sourse_field.split(",");

        searchSourceBuilder.fetchSource(includes, new String[]{});
        searchRequest.source(searchSourceBuilder);


        List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();
        //使用es客户端进行搜索请求Es
        long total = 0;
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            SearchHits hits = searchResponse.getHits();
            total = hits.totalHits;
            //取出一条查询的课程
            SearchHit[] searchHits = hits.getHits();

            for (SearchHit searchHit : searchHits) {
                //获取源文档的内容
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
                String courseid = (String) sourceAsMap.get("courseid");
                String media_id = (String) sourceAsMap.get("media_id");
                String media_url = (String) sourceAsMap.get("media_url");
                String teachplan_id = (String) sourceAsMap.get("teachplan_id");
                String media_fileoriginalname = (String) sourceAsMap.get("media_fileoriginalname");

                teachplanMediaPub.setCourseId(courseid);
                teachplanMediaPub.setMediaUrl(media_url);
                teachplanMediaPub.setMediaFileOriginalName(media_fileoriginalname);
                teachplanMediaPub.setMediaId(media_id);
                teachplanMediaPub.setTeachplanId(teachplan_id);
                //加入数据列表
                teachplanMediaPubList.add(teachplanMediaPub);
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
        //数据集合
        QueryResult<TeachplanMediaPub> queryResult = new QueryResult<>();
        queryResult.setList(teachplanMediaPubList);
        queryResult.setTotal(total);

        return new QueryResponseResult<>(CommonCode.SUCCESS, queryResult);
    }
}
