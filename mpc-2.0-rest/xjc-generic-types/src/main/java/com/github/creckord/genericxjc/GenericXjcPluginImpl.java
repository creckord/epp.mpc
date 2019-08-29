package com.github.creckord.genericxjc;

import java.util.Map;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.outline.Outline;

public final class GenericXjcPluginImpl extends Plugin {

	@Override
	public String getOptionName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void postProcessModel(Model model, ErrorHandler errorHandler) {
		Map<NClass, CClassInfo> beans = model.beans();
		for (CClassInfo classInfo : beans.values()) {
			classInfo.accept(null);
		}
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean run(Outline outline, Options opt, ErrorHandler errorHandler) throws SAXException {
		// TODO Auto-generated method stub
		return false;
	}
}
