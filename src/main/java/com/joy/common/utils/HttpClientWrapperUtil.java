/*
 * @(#)HttpClientWrapperUtil.java 
 *
 * Copyright 2009 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.joy.common.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author zhengsh
 * 
 */
public class HttpClientWrapperUtil {

	/** Log object for this class. */
	private static final Log LOG = LogFactory.getLog( HttpClientWrapperUtil.class );

	private static HttpConnectionManager mtHttpConnMgr = new MultiThreadedHttpConnectionManager();

	private static HttpClientWrapperUtil instance = new HttpClientWrapperUtil();

	public static HttpClientWrapperUtil getInstance() {
		return instance;
	}

	private HttpClientWrapperUtil() {

		HttpConnectionManagerParams httpConnMgrParams = new HttpConnectionManagerParams();
		httpConnMgrParams.setLinger( 0 );
		httpConnMgrParams.setTcpNoDelay( true );

		httpConnMgrParams.setSendBufferSize( 8196 );
		httpConnMgrParams.setReceiveBufferSize( 8196 );
		httpConnMgrParams.setStaleCheckingEnabled( false );

		httpConnMgrParams.setMaxTotalConnections( 500 );
		httpConnMgrParams.setDefaultMaxConnectionsPerHost( 100 );
		mtHttpConnMgr.setParams( httpConnMgrParams );
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> sendPostRequest( String ip, int port, String path, Cookie[] cookies, Map<String, String> params, long timeout, int soTimeout ) throws Exception {

		if ( ip.toLowerCase().startsWith( "http://" ) ) {
			ip = ip.substring( 7 );
		}
		HttpClientParams httpClientParams = new HttpClientParams();
		httpClientParams.setVersion( HttpVersion.HTTP_1_1 );
		httpClientParams.makeLenient();
		httpClientParams.setAuthenticationPreemptive( false );
		httpClientParams.setVirtualHost( ip );

		HttpClient httpClient = new HttpClient( httpClientParams, mtHttpConnMgr );
		httpClientParams.setSoTimeout( soTimeout );
		httpClient.setParams( httpClientParams );
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout( (int) timeout );

		HostConfiguration hostConf = new HostConfiguration();

		hostConf.setHost( ip, port, "http" );
		httpClient.setHostConfiguration( hostConf );

		PostMethod method = new PostMethod();
		method.setFollowRedirects( false );
		method.setDoAuthentication( false );
		method.getProxyAuthState().setAuthAttempted( false );
		method.getProxyAuthState().setAuthRequested( false );
		method.addRequestHeader( "User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)" );
		method.addRequestHeader( "Connection", "Keep-Alive" );
		method.addRequestHeader( "Content-Type", "application/x-www-form-urlencoded;charset=utf-8" );

		method.getParams().setCookiePolicy( CookiePolicy.IGNORE_COOKIES );
		if ( null != cookies ) {
			StringBuffer cookieSb = new StringBuffer();
			for ( Cookie cookie : cookies ) {
				cookieSb.append( cookie.getName() ).append( "=" ).append( cookie.getValue() ).append( "; " );
			}
			String cookieStr = cookieSb.toString();
			if ( cookieStr.length() > 2 ) {
				cookieStr = cookieStr.substring( 0, cookieStr.length() - 2 );
			}
			method.setRequestHeader( "Cookie", cookieStr );
		}

		method.setPath( !path.trim().startsWith( "/" ) ? "/" : "" + path.trim() );
	 // method.getParams().setParameter( HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler( 3, true ) );

		if ( params != null ) {
			Iterator it = params.entrySet().iterator();
			while ( it.hasNext() ) {
				Entry<String, String> entry = (Entry<String, String>) it.next();
				method.addParameter( entry.getKey(), entry.getValue() );
			}
		}

		Map<String, Object> returnMap = new HashMap<String, Object>();
		String response = "";
		try {
			long start = System.currentTimeMillis();

			int statusCode = httpClient.executeMethod( method );

			long end = System.currentTimeMillis();

			if ( ( (double) ( end - start ) / (double) 1000 ) > 1 ) {
				LOG.info( ( (double) ( end - start ) / (double) 1000 ) + ": second!" );
			}
			returnMap.put( "statusCode", statusCode );
			if ( statusCode != HttpStatus.SC_OK ) {
				LOG.error( "Method Failed" + method.getStatusLine() );
				throw new Exception( "Status Code is " + statusCode );
			}
			response = IOUtils.toString( method.getResponseBodyAsStream(), "UTF-8" );
		 // response = method.getResponseBodyAsString();
			returnMap.put( "body", response );
			returnMap.put( "header", method.getResponseHeaders() );

		} catch ( Exception e ) {
			if ( e instanceof java.net.SocketTimeoutException ) {
				LOG.error( "Socket Time Out URL:  http://" + ip + ":" + port + path );
			}
			LOG.error( e.getMessage(), e );
			throw new Exception( "Internal Error" );
		} finally {
			method.releaseConnection();
		}
		return returnMap;
	}

	public Map<String, Object> sendGetRequest( String ip, int port, String path, Header[] headers, Cookie[] cookies, long timeout, int soTimeout ) throws Exception {

		if ( ip.toLowerCase().startsWith( "http://" ) ) {
			ip = ip.substring( 7 );
		}
		HttpClientParams httpClientParams = new HttpClientParams();
		httpClientParams.setVersion( HttpVersion.HTTP_1_1 );
		httpClientParams.makeLenient();
		httpClientParams.setAuthenticationPreemptive( false );
		httpClientParams.setVirtualHost( ip );

		HttpClient httpClient = new HttpClient( httpClientParams, mtHttpConnMgr );
		httpClientParams.setSoTimeout( soTimeout );
		httpClient.setParams( httpClientParams );
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout( (int) timeout );

		HostConfiguration hostConf = new HostConfiguration();

		hostConf.setHost( ip, port, "http" );
		httpClient.setHostConfiguration( hostConf );

		GetMethod method = new GetMethod();
		method.setFollowRedirects( false );
		method.setDoAuthentication( false );
		method.getProxyAuthState().setAuthAttempted( false );
		method.getProxyAuthState().setAuthRequested( false );
		if ( null != headers ) {
			for ( Header header : headers ) {
				method.addRequestHeader( header.getName(), header.getValue() );
			}
		}
		method.getParams().setCookiePolicy( CookiePolicy.IGNORE_COOKIES );
		if ( null != cookies ) {
			StringBuffer cookieSb = new StringBuffer();
			for ( Cookie cookie : cookies ) {
				cookieSb.append( cookie.getName() ).append( "=" ).append( cookie.getValue() ).append( "; " );
			}
			String cookieStr = cookieSb.toString();
			if ( cookieStr.length() > 2 ) {
				cookieStr = cookieStr.substring( 0, cookieStr.length() - 2 );
			}
			method.setRequestHeader( "Cookie", cookieStr );
		}

		method.setPath( !path.trim().startsWith( "/" ) ? "/" : "" + path.trim() );
// method.getParams().setParameter( HttpMethodParams.RETRY_HANDLER, new
		// DefaultHttpMethodRetryHandler( 3, true ) );

// if ( params != null ) {
// Iterator it = params.entrySet().iterator();
// while ( it.hasNext() ) {
// Entry<String, String> e = (Entry<String, String>) it.next();
// method.addParameter( e.getKey(), e.getValue() );
// }
// }

		Map<String, Object> returnMap = new HashMap<String, Object>();
		String response = "";
		try {
//			System.out.println("<<<<<<<<<   uri  = ["+method.getURI().getQuery()+ "]          >>>>>>>>>>>");
			int statusCode = httpClient.executeMethod( method );

			returnMap.put( "statusCode", statusCode );
			if ( statusCode != HttpStatus.SC_OK ) {
				LOG.error( "Method Failed" + method.getStatusLine() );
				throw new Exception( "Status Code is " + statusCode );
			}

			response = IOUtils.toString( method.getResponseBodyAsStream(), "UTF-8" );
// if ( !method.getResponseCharSet().equalsIgnoreCase( "utf-8" ) )
// response = IOUtils.toString( method.getResponseBodyAsStream(), "UTF-8" );
// else
// response = method.getResponseBodyAsString();

			returnMap.put( "body", response );
			returnMap.put( "header", method.getResponseHeaders() );

		} catch ( Exception e ) {
			if ( e instanceof java.net.SocketTimeoutException ) {
				LOG.error( "Socket Time Out URL:  http://" + ip + ":" + port + path );
			}
			LOG.error( e.getMessage(), e );
			throw new Exception( "Internal Error" );
		} finally {
			method.releaseConnection();
		}
		return returnMap;
	}

}
