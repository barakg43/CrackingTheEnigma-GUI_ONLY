package decryptionManager.components;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicLong;

public class  AtomicCounter {

        private final AtomicLong counter;
        private final PropertyChangeSupport pcs ;
        public AtomicCounter() {
            counter= new AtomicLong(0);
            this.pcs = new PropertyChangeSupport(this);
        }

        public void increment() {
            long oldValue = this.counter.get();
            pcs.firePropertyChange("counter", oldValue,  counter.incrementAndGet());
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }


}
