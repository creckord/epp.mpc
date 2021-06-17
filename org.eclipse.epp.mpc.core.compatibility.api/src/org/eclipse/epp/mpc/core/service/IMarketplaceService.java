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
 *     The Eclipse Foundation - initial API and implementation
 *     Yatta Solutions - bug 432803: public API
 *******************************************************************************/
package org.eclipse.epp.mpc.core.service;

import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.epp.mpc.core.model.ICategory;
import org.eclipse.epp.mpc.core.model.IFavoriteList;
import org.eclipse.epp.mpc.core.model.IMarket;
import org.eclipse.epp.mpc.core.model.INews;
import org.eclipse.epp.mpc.core.model.INode;
import org.eclipse.epp.mpc.core.model.ISearchResult;

/**
 * A service that provides access to the marketplace and implements the <a
 * href="https://wiki.eclipse.org/Marketplace/REST">Marketplace REST API</a>.
 *
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 * @author David Green
 * @author Carsten Reckord
 */
public interface IMarketplaceService {

	/**
	 * Property key for registered IMarketplaceService OSGi services indicating the marketplace's base URL.
	 */
	public static final String BASE_URL = "url"; //$NON-NLS-1$

	/**
	 * The base URL of the Marketplace REST API for this service instance. All REST calls will be made against pathes
	 * under this URL.
	 *
	 * @return the base URL of the Marketplace REST API
	 */
	URL getBaseUrl();

	/**
	 * Get a list of all markets. This is the entrypoint to the marketplace.
	 */
	List<? extends IMarket> listMarkets(IProgressMonitor monitor) throws CoreException;

	/**
	 * Get a market by its id
	 *
	 * @param market
	 *            the market which must have an {@link IMarket#getUrl() url}.
	 * @return the identified node
	 */
	IMarket getMarket(IMarket market, IProgressMonitor monitor) throws CoreException;

	/**
	 * Get a category by its id
	 *
	 * @param category
	 *            A category which must have an {@link ICategory#getUrl() url}.
	 * @return the identified category
	 */
	ICategory getCategory(ICategory category, IProgressMonitor monitor) throws CoreException;

	/**
	 * Get a node by its id
	 *
	 * @param node
	 *            the node which must either have an {@link INode#getUrl() url} or an {@link INode#getId() id}.
	 * @return the identified node
	 */
	INode getNode(INode node, IProgressMonitor monitor) throws CoreException;

	/**
	 * Get a list of nodes by their ids. Only existing nodes are returned. Nodes not found on the server will be
	 * discarded in the result list.
	 *
	 * @param nodes
	 *            the nodes which must all have an {@link INode#getId() id}.
	 * @return the identified nodes in iteration order of the {@code nodes} collection
	 */
	List<INode> getNodes(Collection<? extends INode> nodes, IProgressMonitor monitor) throws CoreException;

	/**
	 * Find nodes in the marketplace with a text query, and optionally specify the market/category
	 *
	 * @param market
	 *            the market to search in, or null if the search should span all markets
	 * @param category
	 *            the category to search in, or null if the search should span all categories
	 * @param queryText
	 *            the query text, must not be null
	 * @return the search result
	 */
	ISearchResult search(IMarket market, ICategory category, String queryText, IProgressMonitor monitor)
			throws CoreException;

	/**
	 * Find nodes in the marketplace tagged with the given tag. Only nodes having an exact (case-insensitie) match for
	 * the given tag will be returned.
	 *
	 * @param tag
	 *            a free-form tag
	 * @return the search result
	 */
	ISearchResult tagged(String tag, IProgressMonitor monitor) throws CoreException;

	/**
	 * Find nodes in the marketplace tagged with the given tags. Only nodes having an exact (case-insensitie) match for
	 * at least one of the given tags will be returned.
	 *
	 * @param tags
	 *            free-form tags
	 * @return the search result
	 */
	ISearchResult tagged(List<String> tags, IProgressMonitor monitor) throws CoreException;

	/**
	 * Find featured nodes in the marketplace
	 *
	 * @return the search result
	 */
	ISearchResult featured(IProgressMonitor monitor) throws CoreException;

	/**
	 * Find featured nodes in the marketplace
	 *
	 * @param market
	 *            the market in which to return featured, or null if featured should include all markets
	 * @param category
	 *            the category in which to return fetured, or null if featured should include all categories
	 * @return the search result
	 */
	ISearchResult featured(IMarket market, ICategory category, IProgressMonitor monitor) throws CoreException;

	/**
	 * Find recently added/modified nodes in the marketplace
	 *
	 * @return the search result
	 */
	ISearchResult recent(IProgressMonitor monitor) throws CoreException;

	/**
	 * Find most-favorited nodes in the marketplace
	 *
	 * @return the search result
	 * @deprecated use {@link #topFavorites(IProgressMonitor)} instead
	 */
	@Deprecated
	ISearchResult favorites(IProgressMonitor monitor) throws CoreException;

	/**
	 * Find most-favorited nodes in the marketplace
	 *
	 * @return the search result
	 */
	ISearchResult topFavorites(IProgressMonitor monitor) throws CoreException;

	/**
	 * Find most active nodes in the marketplace
	 *
	 * @return the search result
	 */
	ISearchResult popular(IProgressMonitor monitor) throws CoreException;

	/**
	 * Find recommendations based on a list of other nodes. Usually those will be a list of already installed nodes or
	 * nodes currently flagged for installation.
	 *
	 * @return the search result
	 */
	ISearchResult related(List<? extends INode> basedOn, IProgressMonitor monitor) throws CoreException;

	/**
	 * Find nodes favorited by the user
	 *
	 * @return the search result
	 * @throws CoreException
	 * @throws NotAuthorizedException
	 *             if the user isn't logged in
	 */
	ISearchResult userFavorites(IProgressMonitor monitor) throws CoreException, NotAuthorizedException;

	/**
	 * Set the favorite status of the given nodes for the current user.
	 *
	 * @param nodes
	 *            the nodes to update
	 * @param monitor
	 *            progress and cancellation
	 * @throws CoreException
	 * @throws NotAuthorizedException
	 *             if the user isn't logged in
	 */
	void userFavorites(List<? extends INode> nodes, IProgressMonitor monitor)
			throws CoreException, NotAuthorizedException;

	/**
	 * Retrieve the favorite nodes for the given favorites uri.
	 *
	 * @param favoritesUri
	 *            a url pointing to a favorites list
	 * @param monitor
	 *            progress and cancellation
	 * @throws CoreException
	 */
	ISearchResult userFavorites(URI favoritesUri, IProgressMonitor monitor) throws CoreException;

	List<IFavoriteList> userFavoriteLists(IProgressMonitor monitor) throws CoreException;

	/**
	 * Get the news configuration for the marketplace
	 *
	 * @return the news configuration
	 */
	INews news(IProgressMonitor monitor) throws CoreException;

	/**
	 * Report an error in resolving an install operation.
	 *
	 * @param result
	 *            the status of the install operation
	 * @param nodes
	 *            the nodes that were included in the install, or null if unknown.
	 * @param iuIdsAndVersions
	 *            the IUs and their versions (comma-delimited), or null if unknown.
	 * @param resolutionDetails
	 *            the detailed error message, or null if unknown.
	 * @param monitor
	 * @noreference This method is not intended to be called by clients directly. It should only ever be called as part
	 *              of an install operation.
	 */
	void reportInstallError(IStatus result, Set<? extends INode> nodes, Set<String> iuIdsAndVersions,
			String resolutionDetails, IProgressMonitor monitor) throws CoreException;

	/**
	 * Report a successful install.
	 *
	 * @param node
	 *            the installed node
	 * @param monitor
	 * @noreference This method is not intended to be called by clients directly. It should only ever be called as part
	 *              of an install operation.
	 */
	void reportInstallSuccess(INode node, IProgressMonitor monitor);

	IUserFavoritesService getUserFavoritesService();
}
