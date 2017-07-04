package com.landray.kmss.common.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.landray.kmss.common.dao.HQLInfo;
import com.landray.kmss.common.model.IBaseTreeModel;
import com.landray.kmss.common.service.IBaseService;
import com.landray.kmss.constant.SysAuthConstant;
import com.landray.kmss.sys.authentication.intercept.IAuthenticationValidator;
import com.landray.kmss.sys.authentication.intercept.ValidatorRequestContext;
import com.landray.kmss.sys.config.model.SysConfigParameters;
import com.landray.kmss.util.KmssMessages;
import com.landray.kmss.util.KmssReturnPage;
import com.landray.kmss.util.ModelUtil;
import com.landray.kmss.util.StringUtil;
import com.sunbor.web.tag.Page;

public abstract class TemplateNodeAction extends ExtendAction {
	private IAuthenticationValidator authCategoryEditorValidator = null;

	protected abstract IBaseService getTreeServiceImp(HttpServletRequest request);

	protected abstract String getParentProperty();

	/**
	 * 列出分类下的所有子节点
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward listChildren(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		KmssMessages messages = new KmssMessages();
		try {
			String s_pageno = request.getParameter("pageno");
			String s_rowsize = request.getParameter("rowsize");
			String orderby = request.getParameter("orderby");
			String ordertype = request.getParameter("ordertype");
			String parentId = request.getParameter("parentId");
			String s_IsShowAll = request.getParameter("isShowAll");
			boolean isShowAll = false;
			if (StringUtil.isNotNull(s_IsShowAll) && s_IsShowAll.equals("true"))
				isShowAll = true;
			boolean isReserve = false;
			if (ordertype != null && ordertype.equalsIgnoreCase("down")) {
				isReserve = true;
			}
			int pageno = 0;
			int rowsize = SysConfigParameters.getRowSize();
			if (s_pageno != null && s_pageno.length() > 0) {
				pageno = Integer.parseInt(s_pageno);
			}
			if (s_rowsize != null && s_rowsize.length() > 0) {
				rowsize = Integer.parseInt(s_rowsize);
			}
			if (isReserve)
				orderby += " desc";
			HQLInfo hqlInfo = new HQLInfo();
			hqlInfo.setOrderBy(orderby);
			hqlInfo.setPageNo(pageno);
			hqlInfo.setRowSize(rowsize);
			if (isEditor(request))
				hqlInfo.setAuthCheckType(SysAuthConstant.AUTH_CHECK_NONE);
			changeFindPageHQLInfo(request, hqlInfo);
			String whereBlock = hqlInfo.getWhereBlock();
			if (!StringUtil.isNull(parentId)) {
				if (StringUtil.isNull(whereBlock))
					whereBlock = "";
				else
					whereBlock = "(" + whereBlock + ") and ";
				String tableName = ModelUtil.getModelTableName(getServiceImp(
						request).getModelName());
				if (isShowAll) {
					IBaseTreeModel treeModel = (IBaseTreeModel) getTreeServiceImp(
							request).findByPrimaryKey(parentId);
					whereBlock += tableName + "." + getParentProperty()
							+ ".fdHierarchyId like :_treeHierarchyId";
					hqlInfo.setParameter("_treeHierarchyId", treeModel
							.getFdHierarchyId()
							+ "%");
				} else {
					whereBlock += tableName + "." + getParentProperty()
							+ ".fdId=:_treeParentId";
					hqlInfo.setParameter("_treeParentId", parentId);
				}
				if(StringUtil.isNull(hqlInfo.getOrderBy())){
				  hqlInfo.setOrderBy(tableName + ".fdOrder, " + tableName + ".fdId");
				}
			}
			hqlInfo.setWhereBlock(whereBlock);
			Page page = getServiceImp(request).findPage(hqlInfo);
			request.setAttribute("queryPage", page);
		} catch (Exception e) {
			messages.addError(e);
		}
		if (messages.hasError()) {
			KmssReturnPage.getInstance(request).addMessages(messages)
					.addButton(KmssReturnPage.BUTTON_CLOSE).save(request);
			return mapping.findForward("failure");
		} else {
			return mapping.findForward("listChildren");
		}
	}

	private boolean isEditor(HttpServletRequest request) throws Exception {
		ValidatorRequestContext validatorContext = new ValidatorRequestContext(
				request, "");
		validatorContext.setValidatorPara("cateid", "parentId");
		return getAuthCategoryEditorValidator().validate(validatorContext);
	}

	public IAuthenticationValidator getAuthCategoryEditorValidator() {
		if (authCategoryEditorValidator == null)
			authCategoryEditorValidator = (IAuthenticationValidator) getBean("authCategoryEditorValidator");
		return authCategoryEditorValidator;
	}
}