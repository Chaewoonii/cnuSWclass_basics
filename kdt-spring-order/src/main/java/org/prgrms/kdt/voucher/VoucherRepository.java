package org.prgrms.kdt.voucher;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;
import java.util.UUID;


public interface VoucherRepository {
    Optional<Voucher> findById(UUID voucherId);
    Voucher insert(Voucher voucher);
}
