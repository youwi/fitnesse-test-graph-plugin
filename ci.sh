scp build/libs/*.jar test.jihui.in:/root/fitnesse/plugins/
scp -r src/main/resources/fitnesse/resources/* test.jihui.in:/root/fitnesse/FitNesseRoot/files/fitnesse/
curl test.jihui.in:9090/any?restart
ssh test.jihui.in 'cd /root/fitnesse  && /root/fitnesse/_start.sh >log.log 2>&1'