<!DOCTYPE html>
<html lang="zh-CN">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="content-type" content="no-cache, must-revalidate" />
	<link rel="icon" href="/img/itmooc.ico">
    <title>ITMOOC--${courseBase.name}</title>
</head>
<body data-spy="scroll" data-target="#articleNavbar" data-offset="150">
<!-- 页面头部 -->
<!--#include virtual="/include/header.html"-->
<div id="body">
<!--页面头部结束sss-->
<div class="article-banner">
    <div class="banner-bg"></div>
    <div class="banner-info">
        <div class="banner-left">
            <p class="tit">${courseBase.name}</p>
            <p class="info">
                <a href="http://ucenter.itmooc.com/#/learning/${courseBase.id}/0"  target="_blank" v-if="learnstatus == 1" v-cloak>马上学习</a>
                <a href="#"  @click="addopencourse" v-if="learnstatus == 2" v-cloak>立即报名</a>
                <span><em>难度等级</em>
		 <#if courseBase.grade=='200001'>
		低级
                <#elseif courseBase.grade=='200002'>
		中级
		 <#elseif courseBase.grade=='200003'>
		高级
		</#if>
                </span>

            </p>
        </div>
        <div class="banner-rit">

	    <#if (coursePic.pic)??>
	     <p><img src="http://img.itmooc.com/${coursePic.pic}" alt="" width="270" height="156"> </p>
	    </#if>


        </div>
    </div>
</div>
<div class="article-cont">
    <div class="tit-list">
        <a href="javascript:;" id="articleClass" class="active">课程介绍</a>
        <a href="javascript:;" id="articleItem">目录</a>
    </div>
    <div class="article-box">
        <div class="articleClass" style="display: block">
            <div class="article-cont">
                <div class="article-left-box">
                    <div class="content">

                        <div class="content-com suit">
                            <div class="title"><span>适用人群</span></div>
                            <div class="cont cktop">
                                <div >
                                    <p>${(courseBase.users)!""}</p>
                                </div>
                            </div>
                        </div>
                        <div class="content-com course">
                            <div class="title"><span>课程制作</span></div>
                            <div class="cont">
                                <div class="img-box">

                            	</div>
                                <div class="info-box">
                                    <p class="name">教学方：<em v-text="teacher.name"></em></p>
                                    <p class="info" v-text="teacher.intro"></p>
                                </div>
                            </div>
                        </div>
                        <div class="content-com about">
                            <div class="title"><span>课程介绍</span></div>
                            <div class="cont cktop">
                                <div >
                                    <p>${(courseBase.description)!""}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="article-right-box">
                    <div class="about-teach">
                        <!--机构信息-->
                        <div class="teach-info">
                            <p v-text="office.name"></p>
                        </div>
                        <p class="synopsis" v-text="office.intro"></p>
                    </div>
                </div>

            </div>
        </div>
        <div class="articleItem" style="display: none">
            <div class="article-cont-catalog">
                <div class="article-left-box">
                    <div class="content">
						<#if (teachplanNode.children)??>
                            <#list teachplanNode.children as firstNode>
                                <div class="item">
                                    <div class="title act"><i class="i-chevron-top"></i>${firstNode.pname}</div>
                                    <div class="about">${firstNode.description!}</div>
                                    <div class="drop-down" style="height: ${firstNode.children?size * 50}px;">
                                        <ul class="list-box">
                                            <#list firstNode.children as secondNode>
                                                <li><a href="http://ucenter.itmooc.com/#/learning/${courseBase.id}/${secondNode.id}">${secondNode.pname}</a></li>
                                            </#list>
                                        </ul>
                                    </div>
                                </div>
                            </#list>
                        </#if>
                    </div>
                </div>
                <div class="article-right-box">
                    <div class="about-teach">
                        <!--机构信息-->
                        <div class="teach-info">
                            <p v-text="office.name"></p>
                        </div>
                        <p class="synopsis" v-text="office.intro"></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 页面底部 -->
<!--底部版权-->
<!--#include virtual="/include/footer.html"-->

<!--底部版权-->
</div>
<script>var courseId = "${courseBase.id}"
var teacherId = "${courseBase.userId}"
var officeId = "${courseBase.officeId}"</script>
<!--#include virtual="/include/course_detail_dynamic.html"-->
</body>
</html>
