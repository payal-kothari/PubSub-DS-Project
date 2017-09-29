# How to run this project:

1. cd to PubSubDemo/src/ with following the command,
   * cd PubSubDemo/src/

2. Compile and run EventManager on glados.cs.rit.edu with following the commands,
   * javac edu/rit/CSCI652/impl/*.java
   * java edu.rit.CSCI652.impl.EventManager

3. Run PublisherNode on any RIT's CS machine with the following command,
   * java edu.rit.CSCI652.impl.PublisherNode

4. Run SubscriberNode on any RIT's CS machine with the following command,
   * java edu.rit.CSCI652.impl.SubscriberNode

## Test case 1:
 * On PublisherNode, 
   1. Type 1 to advertise a new topic. Then follow the steps displayed on the console.
   2. Type 2 to publish an event. Then follow the steps displayed on the console.
   
 * On SubscriberNode,
   1. Type 1 to subscribe to a topic name. The console will display the topic list with index numbers. Type the index number of the 
      topic you want to subscribe to. 
   2. Type 2 to subscribe using a keyword. Then type the keyword.
   3. Type 3 to un-subscribe from a particular topic. The console will display the subscribed topics with index numbers. Type the index 
      number of the topic you want to unsubscribe from. 
   4. Type 4 to unsubscribe from all the subscribed topics.
   5. Type 5 to see the list of the subscribed topics.
  

## Test case 2 : 
   1. Subscribe to a topic from the SubscriberNode.
   2. Close the terminal of this SubscriberNode. 
   3. Publish an event under the subscribed topic.
   4. Again run the SubscriberNode(on the same machine, so the IP address will not change). You will see a notification displaying an 
      article published under the topic you had subscribed to.
      
