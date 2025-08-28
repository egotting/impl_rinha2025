#!/bin/bash
cd ~/Documentos/repo/github.com/egotting/impl_rinha2025/payment-processor/
docker system prune
docker compose -f docker-compose.yml up -d --build
cd ..
docker compose up -d --build
lazydocker
