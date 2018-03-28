# CatsDogs

Cats and Dogs test task

The first activity contains 3 lists of image feeds from flickr. 
First list is of images tagged "cat", second "dog" and third, just unfiltered feed.
Images are sorted by date_taken tag date.
Image lists are scrollable horizontally and activity is scrollable vertical.
There's a button that allows refreshing those feeds.
When user clicks on image from any feed it animates into fullscreen view activity using common element animation.
Fullscreen view activity first displays smaller image from previous list, and tries to load higher-res image in background.
If downloading higher-res image succeeds, it seemlessly replaces older one with it. Image loading is done with Picasso library, that caches images automatically.
If user taps the image, additional details will be diplayed, including picture date and tags.
Floating button in the lower right corner opens browser window with current image's page on flickr.

Unit tests and some UI testing is included in the project (although, there, of course, could be more tests).

Enjoy!

