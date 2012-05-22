package com.ngdb.web.services;

import static org.apache.tapestry5.SymbolConstants.APPLICATION_VERSION;

import java.util.ResourceBundle;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.services.AssetSource;

import com.ngdb.web.services.domain.CollectionService;
import com.ngdb.web.services.domain.GameService;
import com.ngdb.web.services.domain.HardwareService;
import com.ngdb.web.services.domain.PictureService;
import com.ngdb.web.services.domain.ReferenceService;
import com.ngdb.web.services.domain.ShopService;
import com.ngdb.web.services.domain.UserService;
import com.ngdb.web.services.domain.WishService;

public class AppModule {

	public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
		String version = ResourceBundle.getBundle("ngdb").getString("version");
		configuration.override(APPLICATION_VERSION, version);
	}

	@Contribute(WebSecurityManager.class)
	public static void addRealms(Configuration<Realm> configuration) {
		BasicRealm realm = new BasicRealm();
		configuration.add(realm);
	}

	public static void contributeComponentMessagesSource(AssetSource assetSource, OrderedConfiguration<Resource> configuration) {
		configuration.add("GlobalCatalogue", assetSource.resourceForPath("ngdb.properties"), "after:AppCatalog");
	}

	public static void contributeHibernateEntityPackageManager(Configuration<String> configuration) {
		configuration.add("com.ngdb.entities");
	}

	public static void bind(ServiceBinder binder) {
		binder.bind(UserService.class);
		binder.bind(PictureService.class);
		binder.bind(GameService.class);
		binder.bind(HardwareService.class);
		binder.bind(WishService.class);
		binder.bind(CollectionService.class);
		binder.bind(ReferenceService.class);
		binder.bind(ShopService.class);
	}
}
