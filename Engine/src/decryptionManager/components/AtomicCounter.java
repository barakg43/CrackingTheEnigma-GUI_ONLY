package decryptionManager.components;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicLong;

public class  AtomicCounter {
        private final String PROPERTY_NAME="counter";
        private final AtomicLong counter;
        private final PropertyChangeSupport pcs ;
        public AtomicCounter() {
            counter= new AtomicLong(0);
            this.pcs = new PropertyChangeSupport(this);
        }

        public void increment() {
            Long oldValue = this.counter.get();
            Long newValue=counter.incrementAndGet();
            pcs.firePropertyChange(PROPERTY_NAME, oldValue, newValue );
        }
        public long getValue()
        {
            return counter.get();
        }
        public void resetCounter()
        {
            counter.set(0);
        }
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(PROPERTY_NAME,listener);
            // pcs.addPropertyChangeListener(listener);
        }


}
