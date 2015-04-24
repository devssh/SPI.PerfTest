kill $(ps aux | grep 'tail -f' | awk '{print $2}')
