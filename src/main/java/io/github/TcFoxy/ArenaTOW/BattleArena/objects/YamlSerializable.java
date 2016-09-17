package io.github.TcFoxy.ArenaTOW.BattleArena.objects;

import java.util.Map;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.exceptions.SerializationException;



public interface YamlSerializable {
	Object yamlToObject(Map<String,Object> map, String value) throws SerializationException;
	Object objectToYaml() throws SerializationException;
}
