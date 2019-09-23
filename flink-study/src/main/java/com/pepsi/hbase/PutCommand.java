package com.pepsi.hbase;

import com.stumbleupon.async.Deferred;
import org.hbase.async.PutRequest;

import java.util.ArrayList;
import java.util.List;

import static com.pepsi.util.FlinkUtils.*;


public class PutCommand implements Command<PutCommand> {

    private SimpleHBaseClient client;

    private byte[] table;
    private byte[] key;
    private byte[] family;
    private List<byte[]> qualifiers;
    private List<byte[]> values;

    public PutCommand(SimpleHBaseClient client) {
        if (client == null) {
            throw new IllegalArgumentException("client should not be empty");
        }
        this.client = client;
    }

    @Override
    public PutCommand execute(long timeout) throws Exception {
        if (checkNonEmpty(table) && checkNonEmpty(key) && checkNonEmpty(family) && checkNonEmpty(qualifiers) && checkNonEmpty(values)) {
            PutRequest req = new PutRequest(table, key, family, toArray(qualifiers), toArray(values));
            Deferred<Object> defer = client.getHBase().put(req);
            if (timeout > 0) {
                defer.join(timeout);
            }
        }
        return this;
    }

    public PutCommand flush() throws Exception {
        client.getHBase().flush().join(1000);
        return this;
    }

    public PutCommand setTable(String value) {
        if (checkNonEmpty(value)) {
            this.table = value.getBytes(getCharset());
        }
        return this;
    }

    public PutCommand setTable(byte[] value) {
        if (checkNonEmpty(value)) {
            this.table = value;
        }
        return this;
    }

    public PutCommand setKey(String value) {
        if (checkNonEmpty(value)) {
            this.key = value.getBytes(getCharset());
        }
        return this;
    }

    public PutCommand setKey(byte[] value) {
        if (checkNonEmpty(value)) {
            this.key = value;
        }
        return this;
    }

    public PutCommand setFamily(String value) {
        if (checkNonEmpty(value)) {
            this.family = value.getBytes(getCharset());
        }
        return this;
    }

    public PutCommand setFamily(byte[] value) {
        if (checkNonEmpty(value)) {
            this.family = value;
        }
        return this;
    }

    public PutCommand addData(byte[] qualifier, byte[] value) {
        if (checkNonEmpty(qualifier) && checkNonEmpty(value)) {
            if (this.qualifiers == null) {
                this.qualifiers = new ArrayList<>();
            }
            this.qualifiers.add(qualifier);

            if (this.values == null) {
                this.values = new ArrayList<>();
            }
            this.values.add(value);
        }
        return this;
    }

    public PutCommand addData(String qualifier, String value) {
        if (checkNonEmpty(qualifier) && checkNonEmpty(value)) {
            addData(qualifier.getBytes(getCharset()), value.getBytes(getCharset()));
        }
        return this;
    }
}
