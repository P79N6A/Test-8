#/bin/bash

#SKU赠送后台 
ps -ef | grep SKUPresentApp| grep -v 'grep' | awk -F ' ' '{print $2}' | xargs kill -9
