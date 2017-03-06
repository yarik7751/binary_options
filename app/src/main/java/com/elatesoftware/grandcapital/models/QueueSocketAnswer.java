package com.elatesoftware.grandcapital.models;

import com.elatesoftware.grandcapital.api.pojo.SocketAnswer;
import com.elatesoftware.grandcapital.views.fragments.TerminalFragment;

/**
 * Created by Дарья Высокович on 03.03.2017.
*/
public class QueueSocketAnswer {

    private ObjectBox head = null;      // Указатель на первый элемент
    private ObjectBox tail = null;      // Указатель на последний элемент
    private int size = 0;        // Поле для хранения размера очереди

    private static QueueSocketAnswer queue = null;
    public static QueueSocketAnswer getInstance() {
        if (queue == null) {
            queue = new QueueSocketAnswer();
        }
        return queue;
    }

    public void push(SocketAnswer obj) {
        ObjectBox ob = new ObjectBox();
        ob.setObject(obj);
        if (head == null) {
            head = ob;
        } else {
            tail.setNext(ob);
        }
        tail = ob;
        size++;
    }

    public SocketAnswer pull() {
        if (size == 0) {
            return null;
        }
        SocketAnswer obj = head.getObject();
        head = head.getNext();
        if (head == null) {
            tail = null;
        }
        size--;
        return obj;
    }

    public SocketAnswer get(int index) {
        if(size == 0 || index >= size || index < 0) {
            return null;
        }
        ObjectBox current = head;
        int pos = 0;
        while(pos < index) {
            current = current.getNext();
            pos++;
        }
        SocketAnswer  obj = current.getObject();
        return obj;
    }

    public int size() {
        return size;
    }

    private class ObjectBox
    {
        private SocketAnswer  object;
        private ObjectBox next;

        public SocketAnswer getObject() {
            return object;
        }

        public void setObject(SocketAnswer object) {
            this.object = object;
        }

        public ObjectBox getNext() {
            return next;
        }

        public void setNext(ObjectBox next) {
            this.next = next;
        }
    }
}