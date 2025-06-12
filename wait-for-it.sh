#!/usr/bin/env sh
# wait-for-it.sh

host="$1"
shift
port="$1"
shift

until nc -z "$host" "$port"; do
  echo "Waiting for $host:$port..."
  sleep 2
done

exec "$@" 