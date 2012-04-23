package com.micro4blog.plugin;

/**
 * 关于java.lang.IllegalAccessError: Class ref in pre-verified class resolved to unexpected implementation
 * ieda使用不同的scope
 * eclipse使用add library
 * 不能带接口文件，jar优化重新生成jar
 * dx --dex --output
 * @author thom
 *
 */

public interface IPlugin {

	public void addPlugin();
	
}
