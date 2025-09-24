#!/bin/bash
cd ~/Documentos/projects/java/impl_rinha2025/payment-processor/
docker system prune
docker compose -f docker-compose.yml up -d --build
cd ..
docker compose up -d --build
lazydocker