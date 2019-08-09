ps -ef | grep b-interface-0.0.1.jar|grep -v grep|cut -c 9-15|xargs kill -9
