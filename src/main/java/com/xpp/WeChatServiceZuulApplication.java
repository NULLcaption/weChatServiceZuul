package com.xpp;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;

@EnableZuulProxy
@SpringBootApplication
public class WeChatServiceZuulApplication {

	@Value("${home:}")
	private String home;

	@Value("${https.ssl.port}")
	private Integer port;

	@Value("${https.ssl.key-store}")
	private String keyStore;

	@Value("${https.ssl.key-alias:}")
	private String keyAlias;

	@Value("${https.ssl.key-store-password}")
	private String keyStorePassword;

	@Value("${https.ssl.key-store-type}")
	private String keyStoreType;

	public static void main(String[] args) {
		SpringApplication.run(WeChatServiceZuulApplication.class, args);
		System.out.println("    ____  ______________  ________________  __  ___   _______   ________\n" +
				"   / __ )/  _/ ____/ __ )/ ____/ ____/ __ \\/ / / / | / /  _/ | / / ____/\n" +
				"  / __  |/ // / __/ __  / __/ / __/ / /_/ / / / /  |/ // //  |/ / / __  \n" +
				" / /_/ // // /_/ / /_/ / /___/ /___/ _, _/ /_/ / /|  // // /|  / /_/ /  \n" +
				"/_____/___/\\____/_____/_____/_____/_/ |_|\\____/_/ |_/___/_/ |_/\\____/   \n" +
				"                                                                        ");
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
		tomcat.addAdditionalTomcatConnectors(httpConnector());
		return tomcat;
	}

	@Bean
	public Connector httpConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();

		ClassPathResource resource = new ClassPathResource(keyStore);
		// 临时目录
		String tempPath =System.getProperty("java.io.tmpdir") + System.currentTimeMillis()+".keystore";
		File f = new File(tempPath);
		System.out.println(".keystore tempPath" + tempPath);

		try {
			// 将key的值转存到临时文件
			IOUtils.copy(resource.getInputStream(), new FileOutputStream(f));
			connector.setScheme("https");
			connector.setSecure(true);
			connector.setPort(port);
			protocol.setKeystoreFile(f.getAbsolutePath());
			protocol.setKeystorePass(keyStorePassword);
			protocol.setKeystoreType(keyStoreType);
			protocol.setSSLEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connector;
	}

}
