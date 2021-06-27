package com.telerikacademy.web.deliver_it.services.contracts.sub;

import com.telerikacademy.web.deliver_it.models.SecurityCredentials;

public interface DeleteService <E>{

    E delete(int id, SecurityCredentials sc );
}
