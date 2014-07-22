###Eclipse trick to write andeoid app without Fragments###

__Steps:__

1. Navigate to your SDK folder then \tools\templates\activities.

2. Copy the __BlankActivity__ folder and paste it into the same directory.

3. Rename __BlankActivity(copy)__ to __EmptyActivity__

4. Copy files from: https://android-review.googlesource.com/#/c/88890/4 to __EmptyActivity__.

5. Select EmptyActivity next time you create a project.


###Problems with Appcompat in __res__###

__Steps:__

1. File->Import->Existing Android Code into Workspace(android-sdk\extras\android\support\v7). Choose "appcompat"

2. Project-> properties->Android. In the section library "Add" and choose "appCompat"

3. Clean the project.

