package io.pivotal.bds.gemfire.fraud.common.key;

import io.pivotal.bds.gemfire.keyfw.BaseDSColocationKey;

@SuppressWarnings("serial")
public class TransactionKey extends BaseDSColocationKey<String, String> {

    public TransactionKey() {
    }

    public TransactionKey(String id, String acctId) {
        super(id, acctId);
    }

}
