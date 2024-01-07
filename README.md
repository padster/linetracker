# linetracker


## TODO:

* Flags for local run vs image run vs cloud run
* Const fixes:
** Don't link to const/undefined in child list.
** Don't fetch from /_/values/const/undefined
* Other fixes:
** Sort single list most recent first
** Implement bulk entry
** Popup on graph dot click
** Fix $NaN.undefined for the estimator
** Clean up LoginPage
* Set up proper health checking via nginx forward to java.
* Deploy
** linetracker.useit.today redirection
* Email all users with queries about what google account to migrate to.
* Rerun the backup script to get a new snapshot of data.
* Run the migration
* Turn off the old appengine app
