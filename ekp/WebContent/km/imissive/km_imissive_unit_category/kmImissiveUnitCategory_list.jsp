<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/resource/jsp/list_top.jsp"%>
<html:form action="/km/imissive/km_imissive_unit_category/kmImissiveUnitCategory.do">
	<div id="optBarDiv">
		<kmss:auth requestURL="/km/imissive/km_imissive_unit_category/kmImissiveUnitCategory.do?method=add" requestMethod="GET">
			<input type="button" value="<bean:message key="button.add"/>"
				onclick="Com_OpenWindow('<c:url value="/km/imissive/km_imissive_unit_category/kmImissiveUnitCategory.do" />?method=add');">
		</kmss:auth>
		<kmss:auth requestURL="/km/imissive/km_imissive_unit_category/kmImissiveUnitCategory.do?method=deleteall" requestMethod="GET">
			<input type="button" value="<bean:message key="button.delete"/>"
				onclick="if(!List_ConfirmDel())return;Com_Submit(document.kmImissiveUnitCategoryForm, 'deleteall');">
		</kmss:auth>
	</div>
<c:if test="${queryPage.totalrows==0}">
	<%@ include file="/resource/jsp/list_norecord.jsp"%>
</c:if>
<c:if test="${queryPage.totalrows>0}">
	<%@ include file="/resource/jsp/list_pagenav_top.jsp"%>
	<table id="List_ViewTable">
		<tr>
			<sunbor:columnHead htmlTag="td">
				<td width="10pt"><input type="checkbox" name="List_Tongle"></td>
				<td width="40pt"><bean:message key="page.serial"/></td>
				<sunbor:column property="kmImissiveUnitCategory.fdName">
					<bean:message  bundle="km-imissive" key="kmImissiveUnitCategory.fdName"/>
				</sunbor:column>
				<sunbor:column property="kmImissiveUnitCategory.fdIsAvailable">
					<bean:message  bundle="km-imissive" key="kmImissiveUnitCategory.fdIsAvailable"/>
				</sunbor:column>
				<sunbor:column property="kmImissiveUnitCategory.docCreator.fdName">
					<bean:message  bundle="km-imissive" key="kmImissiveUnitCategory.docCreateId"/>
				</sunbor:column>
				<sunbor:column property="kmImissiveUnitCategory.docCreateTime">
					<bean:message  bundle="km-imissive" key="kmImissiveUnitCategory.docCreateTime"/>
				</sunbor:column>
			</sunbor:columnHead>
		</tr>
		<c:forEach items="${queryPage.list}" var="kmImissiveUnitCategory" varStatus="vstatus">
			<tr
				kmss_href="<c:url value="/km/imissive/km_imissive_unit_category/kmImissiveUnitCategory.do" />?method=view&fdId=${kmImissiveUnitCategory.fdId}">
				<td>
					<input type="checkbox" name="List_Selected" value="${kmImissiveUnitCategory.fdId}">
				</td>
				<td>${vstatus.index+1}</td>
				<td>
					<c:out value="${kmImissiveUnitCategory.fdName}" />
				</td>
				<td>
					<sunbor:enumsShow value="${kmImissiveUnitCategory.fdIsAvailable}" enumsType="common_yesno" />
				</td>
				<td>
					<c:out value="${kmImissiveUnitCategory.docCreator.fdName}" />
				</td>
				<td>
					<kmss:showDate value="${kmImissiveUnitCategory.docCreateTime}" type="datetime"/>
				</td>
			</tr>
		</c:forEach>
	</table>
	<%@ include file="/resource/jsp/list_pagenav_down.jsp" %>
</c:if>
</html:form>
<%@ include file="/resource/jsp/list_down.jsp"%>