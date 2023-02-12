MODULES="database proxy backend client"


for MODULE in ${MODULES// / }
do
  echo "I am deploying $MODULE ;)"
done
