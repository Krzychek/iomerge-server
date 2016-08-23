# iomerge-server
This is server application of IOMerge project.

Application enables user to control Android device without taking your hands of keyboard and mouse connected to local computer. 
Everything is done through virtually **merging both workspaces** in such a way, that it feels like **your mobile's display would be directly 
one more display connected directly to local computer.**

## To the point:
**Just move your mouse pointer through the edge of your desktop to control your Android phone.**

# !!!BETA!!!
Project is in a beta state, so I'm not releasing it for now.

## Why beta?
There are few things to improve in code base, especially plugins:
- there is done dirty chaining of plugin classloaders to allow spring to laod it. It smells like hell and should be refactored
- also plugins beans class have to be uniq due to spring it should be futher separated


