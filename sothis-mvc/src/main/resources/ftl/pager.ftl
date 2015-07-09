<#ftl attributes={"content_type":"text/html; charset=UTF-8"}>
<#setting number_format="0"> 
<#if theme == "search" >
	<div class="pagination-page right">
		<#if currentPage &gt; 1>
			<a href="${firstPageUrl}" title="第一页" class="page-num fsong">第一页</a>
		</#if>
		<#list pageUrls as pageUrl>
			<#if totalPages &gt; 1>
				<#if pageUrl == "currentPage">
					<a class="current-page" title="第${currentPage}页">${currentPage}</a>
				<#else>
					<a href="${pageUrl}" class="page-num" title="第${pageIndex[pageUrl_index]}页">${pageIndex[pageUrl_index]}</a>
				</#if>
			</#if>
		</#list>
		
		<#if currentPage &lt; totalPages >
			<a href="${nextPageUrl}" title="下一页" class="next" rel="nofollow"><b></b>下一页</a>
		</#if>
	</div>
<#elseif (theme == "topSearch")>
	<#if (totalPages>0)>
		<div class="fr tr pageinc2">
			<span class="mr5">${currentPage}/${totalPages}</span>
			<#if currentPage &gt; 1>
				<a class="page-num fsong" title="首页" href="${firstPageUrl}">首页</a>
				<a class="previous" title="上一页" href="${prePageUrl}" rel="nofollow">上一页</a>
			</#if>
			<#if currentPage &lt; totalPages >
				<a class="next" title="下一页" href="${nextPageUrl}" rel="nofollow">下一页</a>
				<#--
				<a class="page-num fsong" title="末页" href="${lastPageUrl}">末页</a> 
				-->
			</#if>
		</div>
	</#if>
<#elseif (theme == "wapSearch")>
	<#if (totalPages>0)>
		<div class="page">
		  	第${currentPage}页 &nbsp;
			<#if currentPage &gt; 1>
				<a href="${firstPageUrl}">首页</a>
				&nbsp;
				<a href="${prePageUrl}">上一页</a>
				&nbsp;
			</#if>
			<#if currentPage &lt; totalPages >
				<a href="${nextPageUrl}">下一页</a>
				<#--
				&nbsp;
				<a href="${lastPageUrl}">尾页</a>
				-->
			</#if>
		</div>
	</#if>
<#elseif theme == "full">
	<div class="pagination-page right">
		<#if currentPage &gt; 1>
			<#if theme != "simple" >
				<a href="${firstPageUrl}" title="首页" class="page-num fsong">&lt;&lt;</a>
			</#if>
				<a href="${prePageUrl}" title="上一页" class="previous" rel="nofollow"><b></b>上一页</a>
			<#else>
		</#if>
		
		<#list pageUrls as pageUrl>
			<#if pageUrl == "currentPage">
				<a class="current-page" title="第${currentPage}页">${currentPage}</a>
			<#else>
				<a href="${pageUrl}" class="page-num" title="第${pageIndex[pageUrl_index]}页">${pageIndex[pageUrl_index]}</a>
			</#if>
		</#list>
		
		<#if currentPage &lt; totalPages >
			<a href="${nextPageUrl}" title="下一页" class="next" rel="nofollow"><b></b>下一页</a>
			<a href="${lastPageUrl}" title="末页" class="page-num fsong">&gt;&gt;</a>
		</#if>
	</div>
<#else>
	<#if theme == "simple" >
		<div class="pagination-simple right">
			<span>${currentPage}/${totalPages}</span>
	<#else>
		<div class="pagination-page right">
	</#if>
		<#if currentPage &gt; 1>
		<#if theme != "simple" >
			<a href="${firstPageUrl}" title="首页" class="page-num fsong">&lt;&lt;</a>
		</#if>
			<a href="${prePageUrl}" title="上一页" class="previous" rel="nofollow"><b></b>上一页</a>
		<#else>
			
		</#if>
		
		<#if theme == "all" && totalPages != 1 >
			<#list pageUrls as pageUrl>
				<#if pageUrl == "currentPage">
					<a class="current-page" title="第${currentPage}页">${currentPage}</a>
				<#else>
					<a href="${pageUrl}" class="page-num" title="第${pageIndex[pageUrl_index]}页">${pageIndex[pageUrl_index]}</a>
				</#if>
			</#list>
		<#else>
			<#if totalPages != 1 >
				<a title="第${currentPage}页" class="page-num">${currentPage}</a>
			</#if>
		</#if>
		
		<#if currentPage &lt; totalPages >
			<a href="${nextPageUrl}" title="下一页" class="next" rel="nofollow"><b></b>下一页</a>
			<#if theme != "simple" >
			<#--
			<a href="${lastPageUrl}" title="末页" class="page-num fsong">&gt;&gt;</a>
			 -->
		</#if>
		<#else>
			
		</#if>
	</div>
</#if>