/**
 * 
 */
package com.joy.web.common.config;

/**
 * @author shenghui
 *
 */
public class HljiaGlobalVariable extends GlobalVariable {
	
	public static final String TMP_UPLOAD_FILE = getPropertyConfig().getString("tmp.upload.file");
	
	public static final String DINGDING_URL = getPropertyConfig().getString("dingding.url");
	public static final String DINGDING_CLIENT_ID = getPropertyConfig().getString("dingding.clientId");
	public static final String DINGDING_CLIENT_SECRET = getPropertyConfig().getString("dingding.clientSecret");
	
	public static final String GUOJIA_URL = getPropertyConfig().getString("guojia.url");
	public static final String GUOJIA_ACCOUNT = getPropertyConfig().getString("guojia.account");
	public static final String GUOJIA_PASSWORD = getPropertyConfig().getString("guojia.password");
	public static final String GUOJIA_VERSION = getPropertyConfig().getString("guojia.version");
	public static final String GUOJIA_TRANS_ACCOUNT = getPropertyConfig().getString("guojia.transAccount");
	public static final String GUOJIA_DESKEY = getPropertyConfig().getString("guojia.deskey");
	
}
