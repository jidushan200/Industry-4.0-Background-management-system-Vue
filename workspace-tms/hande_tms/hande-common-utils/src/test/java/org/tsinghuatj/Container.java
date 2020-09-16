package org.tsinghuatj;

import cn.org.rapid_framework.generator.GeneratorFacade;

public class Container {
	final static GeneratorFacade facade = new GeneratorFacade();

	static {
		facade.getGenerator().addTemplateRootDir("src/test/template/domain");
		facade.getGenerator().addTemplateRootDir("src/test/template/repository");
		facade.getGenerator().setSourceEncoding("UTF-8");
		facade.getGenerator().setOutputEncoding("UTF-8");
		facade.getGenerator().setOutRootDir("src/test/out");
		facade.getGenerator().setCopyBinaryFile(false);
	}

	public static void main(String[] args) throws Exception {
		facade.deleteOutRootDir();		
		facade.generateByTable("t_tool","t_tool_appendix","t_tool_check","t_tool_coat","t_tool_outbound","t_tool_process","t_tool_repair","t_tool_unqualified_apply","t_tool_unusual","t_tool_warehouse");
		
	}
	

}