/*******************************************************************************
 * Copyright (c) 2014, 2018 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation, bug 432803: public API
 *******************************************************************************/
package org.eclipse.epp.internal.mpc.core.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.epp.internal.mpc.core.MarketplaceClientCore;
import org.eclipse.osgi.util.NLS;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentConstants;

/**
 * @author Carsten Reckord
 */
public class ServiceUtil {

	/**
	 * Set the provided priority on a dictionary for an OSGi service registration. If the dictionary is null,
	 * a new one will be created and returned.
	 *
	 * @param priority the service priority
	 * @param dict the dictionary to change or null
	 * @return the provided dictionary, or a new one if the provided was null, containing a service ranking entry
	 * @see Constants#SERVICE_RANKING
	 */
	public static Dictionary<String, Object> serviceRanking(int priority, Dictionary<String, Object> dict) {
		if (dict == null) {
			dict = new Hashtable<>();
		}
		dict.put(Constants.SERVICE_RANKING, priority);
		return dict;
	}

	public static int getServiceRanking(ServiceReference<?> reference) {
		if (reference == null) {
			return 0;
		}
		Object ranking = reference.getProperty(Constants.SERVICE_RANKING);
		if (ranking instanceof Integer) {
			return (Integer) ranking;
		}
		return 0;
	}

	public static Dictionary<String, Object> higherServiceRanking(ServiceReference<?> reference,
			Dictionary<String, Object> dict) {
		int ranking = getServiceRanking(reference);
		if (reference != null) {
			ranking++;
		}
		return serviceRanking(ranking, dict);
	}

	/**
	 * Set the provided component name on a dictionary for an OSGi service registration. If the dictionary is null, a
	 * new one will be created and returned.
	 *
	 * @param priority
	 *            the service priority
	 * @param dict
	 *            the dictionary to change or null
	 * @return the provided dictionary, or a new one if the provided was null, containing a service ranking entry
	 * @see ComponentConstants#COMPONENT_NAME
	 */
	public static Dictionary<String, Object> serviceName(String name, Dictionary<String, Object> dict) {
		if (dict == null) {
			dict = new Hashtable<>();
		}
		dict.put(ComponentConstants.COMPONENT_NAME, name);
		return dict;
	}

	/**
	 * Get a URL from a property map for the given key. If no entry exists for the key, or the value is not a string or
	 * it can't be parsed into an URL, the default value is returned.
	 *
	 * @param properties
	 *            the map to extract a URL from
	 * @param key
	 *            the key to search under
	 * @param defaultValue
	 *            the default value returned if the entry does not exist or can't be converted to a URL
	 * @return an URL matching the string value found in the map under the given key, or the default value.
	 */
	public static URL getUrl(Map<?, ?> properties, String key, URL defaultValue) {
		if (properties != null) {
			Object value = properties.get(key);
			if (value instanceof String) {
				try {
					return URLUtil.toURL((String) value);
				} catch (MalformedURLException e) {
					MarketplaceClientCore.error(e);
				}
			} else if (value != null) {
				//wrong type, ignore
				MarketplaceClientCore.getLog().log(
						new Status(IStatus.WARNING, MarketplaceClientCore.BUNDLE_ID, NLS.bind(
								Messages.ServiceUtil_ignoringIncompatibleServiceProperty, value, key)));
			}
		}
		return defaultValue;
	}

	/**
	 * Parse the given string to an URL. If the string can't be parsed, an error is logged
	 * and null is returned
	 *
	 * @param url the url string to parse
	 * @return the value as an URL, or null if there was a parse error
	 */
	public static URL parseUrl(String url) {
		try {
			return URLUtil.toURL(url);
		} catch (MalformedURLException e) {
			MarketplaceClientCore.error(e);
			return null;
		}
	}

	public static Object getOverridablePropertyValue(Map<?, ?> properties, String key) {
		String overridePropertyKey = key + "Property"; //$NON-NLS-1$
		Object overrideProperty = properties.get(overridePropertyKey);
		Object value = overrideProperty == null ? null : System.getProperty(overrideProperty.toString());
		if (value == null) {
			value = properties.get(key);
		}
		return value;
	}

	public static Object getOverridablePropertyValue(ServiceReference<?> serviceReference, String key) {
		String overridePropertyKey = key + "Property"; //$NON-NLS-1$
		Object overrideProperty = serviceReference.getProperty(overridePropertyKey);
		Object value = overrideProperty == null ? null : System.getProperty(overrideProperty.toString());
		if (value == null) {
			value = serviceReference.getProperty(key);
		}
		return value;
	}

	public static Dictionary<String, Object> getProperties(ServiceReference<?> serviceReference) {
		Hashtable<String, Object> properties = new Hashtable<>();
		String[] propertyKeys = serviceReference.getPropertyKeys();
		for (String key : propertyKeys) {
			Object value = serviceReference.getProperty(key);
			properties.put(key, value);
		}
		return properties;
	}

	public static BundleContext getBundleContext(ServiceRegistration<?> registration) {
		ServiceReference<?> reference = registration.getReference();
		return reference == null ? null : getBundleContext(reference);
	}

	public static BundleContext getBundleContext(ServiceReference<?> reference) {
		Bundle bundle = reference.getBundle();
		return bundle != null ? bundle.getBundleContext() : null;
	}

	public static <T> T getService(ServiceReference<T> reference) {
		BundleContext bundleContext = getBundleContext(reference);
		return bundleContext != null ? bundleContext.getService(reference) : null;
	}

	public static <T> T getService(ServiceRegistration<T> registration) {
		ServiceReference<T> reference = registration.getReference();
		return reference == null ? null : getService(reference);
	}

	public static <T> T getService(Class<?> context, Class<T> serviceType) {
		BundleContext bundleContext = FrameworkUtil.getBundle(context).getBundleContext();
		ServiceReference<T> serviceReference = bundleContext == null ? null
				: bundleContext.getServiceReference(serviceType);
		return serviceReference == null ? null : bundleContext.getService(serviceReference);
	}
}
