FROM mongo:5.0.2
COPY --chown=mongodb:mongodb initdb.d/* /docker-entrypoint-initdb.d/
ENV MONGO_INITDB_ROOT_USERNAME=root
ENV MONGO_INITDB_ROOT_PASSWORD=root
CMD ["mongod", "--wiredTigerCacheSizeGB=1"]
HEALTHCHECK --start-period=30s --interval=30s --timeout=10s \
  CMD mongosh --quiet "$(hostname --ip-address || echo '127.0.0.1')/test" --eval 'quit(db.runCommand({ ping: 1 }).ok ? 0 : 2)' || exit 1
