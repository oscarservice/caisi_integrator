/**
 * This package contains classes to upgrade a database schema from one version to it's next.
 * 
 * Each class should do the following : 
 * 	- implement UpgradePatchInterface
 * 	- in general these patches are chained so they should only upgrade 1 version at a time, i.e. n to n+1
 * 	- the class name must be UpgradeFrom<n>to<n+1> as these classes will be auto-detected
 * 	- each upgrade() call will be provided with it's own transaction, in theory throwing and exception on error is sufficient to roll back, as a reminder, this means any JPA exceptions will invalidate the transaction.
 *  - since the upgrades need to be independent of changes to the JPA objects, there should be no jpa model object access but raw jdbc / nativeQueries instead.
 *  - since the changes need to be independent of any other code changes, all values should be hard coded instead of referenced via constants too, i.e. static finaly int MY_NUMBER, might be 0 in one version and maybe 4 in another, so the upgrades should not reference the variable but be hard coded instead.
 *  - any error should throw an exception, it will allow the calling code to abort subsequent upgrades along the chain.
 */
package org.oscarehr.caisi_integrator.dao.schema.upgrade_patches;