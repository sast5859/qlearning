import gym
from gym.envs.registration import register


register(
    id='gym_foo-v0',
    entry_point='gym_foo.envs:FooEnv',
)